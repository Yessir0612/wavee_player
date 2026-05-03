package com.vibewave.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.LibraryMusic
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vibewave.AppRootViewModel
import com.vibewave.R
import com.vibewave.domain.model.Track
import com.vibewave.ui.components.MiniPlayer
import com.vibewave.ui.components.VibeBottomBar
import com.vibewave.ui.navigation.BottomDest
import com.vibewave.ui.navigation.Route
import com.vibewave.ui.screens.auth.AuthScreen
import com.vibewave.ui.screens.auth.AuthViewModel
import com.vibewave.ui.screens.auth.FormState
import com.vibewave.ui.screens.auth.WelcomeScreen
import com.vibewave.ui.screens.home.HomeScreen
import com.vibewave.ui.screens.library.LibraryScreen
import com.vibewave.ui.screens.player.PlayerScreen
import com.vibewave.ui.screens.player.PlayerViewModel
import com.vibewave.ui.screens.profile.ProfileScreen
import com.vibewave.ui.screens.search.SearchScreen
import com.vibewave.ui.theme.VibeWaveTheme
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze

/**
 * Root of the Compose UI.
 *
 * Gating:
 *   - User not signed in → Auth flow (Welcome → Login or Register)
 *   - User signed in → Main app (Home/Search/Profile/Player + mini-player)
 *
 * The auth state comes from [AuthViewModel.currentUser] which listens to
 * FirebaseAuth; any sign-in/out will cause this composable to recompose
 * and switch between the two sub-graphs.
 */
@Composable
fun VibeWaveApp() {
    val authVm: AuthViewModel = hiltViewModel()
    val user by authVm.currentUser.collectAsStateWithLifecycle()

    Crossfade(targetState = user != null, label = "authGate") { signedIn ->
        if (signedIn) MainApp() else AuthFlow()
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Auth sub-graph
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun AuthFlow() {
    val nav = rememberNavController()

    NavHost(
        navController = nav,
        startDestination = Route.Welcome.path,
    ) {
        composable(Route.Welcome.path) {
            WelcomeScreen(
                onLogin = { nav.navigate(Route.Login.path) },
                onSignUp = { nav.navigate(Route.Register.path) },
            )
        }
        composable(Route.Login.path) {
            AuthScreen(
                startMode = FormState.Mode.LOGIN,
                onBack = { nav.popBackStack() },
                // No need to manually navigate — root Crossfade picks up
                // the signed-in state and swaps to MainApp automatically.
                onAuthenticated = {},
            )
        }
        composable(Route.Register.path) {
            AuthScreen(
                startMode = FormState.Mode.REGISTER,
                onBack = { nav.popBackStack() },
                onAuthenticated = {},
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Main app sub-graph
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun MainApp() {
    val colors = VibeWaveTheme.colors
    val nav = rememberNavController()
    val hazeState = remember { HazeState() }

    val backStack by nav.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route
    val onPlayer = currentRoute == Route.Player.path

    val bottomItems = listOf(
        BottomDest(Route.Home, stringResource(R.string.nav_home), Icons.Rounded.GraphicEq),
        BottomDest(Route.Search, stringResource(R.string.nav_search), Icons.Rounded.Search),
        BottomDest(Route.Library, stringResource(R.string.nav_library), Icons.Rounded.LibraryMusic),
        BottomDest(Route.Profile, stringResource(R.string.nav_profile), Icons.Rounded.Person),
    )

    val rootVm: AppRootViewModel = hiltViewModel()
    val settings by rootVm.settings.collectAsStateWithLifecycle()
    val avatarUri = settings?.avatarUri

    val playerVm: PlayerViewModel = hiltViewModel()
    val currentTrack by playerVm.currentTrack.collectAsStateWithLifecycle()
    val isPlaying by playerVm.isPlaying.collectAsStateWithLifecycle()
    val progressFrac by playerVm.progressFraction.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize().background(colors.background)) {

        // Nav content
        NavHost(
            navController = nav,
            startDestination = Route.Home.path,
            modifier = Modifier.fillMaxSize().haze(hazeState),
        ) {
            mainGraph(
                onPlay = { track, queue ->
                    playerVm.play(track, queue)
                    nav.navigate(Route.Player.path)
                },
                onOpenSearch = { nav.navigate(Route.Search.path) },
                onBack = { nav.popBackStack() },
            )
        }

        // Status bar gradient scrim — fades content into the status bar
        // so there's no hard cut between app content and system UI.
        val statusBarHeight = WindowInsets.statusBars.asPaddingValues()
            .calculateTopPadding()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(statusBarHeight + 32.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            colors.surface.copy(alpha = 0.95f),
                            Color.Transparent,
                        )
                    )
                )
        )

        // Mini-player + bottom bar
        AnimatedVisibility(
            visible = !onPlayer,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            Column {
                AnimatedVisibility(
                    visible = currentTrack != null,
                    enter = slideInVertically { it } + fadeIn(),
                    exit = slideOutVertically { it } + fadeOut(),
                ) {
                    MiniPlayer(
                        hazeState = hazeState,
                        track = currentTrack,
                        isPlaying = isPlaying,
                        progress = progressFrac,
                        onClick = { nav.navigate(Route.Player.path) },
                        onPlayPause = { playerVm.toggle() },
                        onNext = { playerVm.next() },
                    )
                }
                VibeBottomBar(
                    hazeState = hazeState,
                    destinations = bottomItems,
                    currentRoute = currentRoute,
                    avatarUri = avatarUri,
                    onClick = { dest ->
                        nav.navigate(dest.route.path) {
                            popUpTo(nav.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        }
    }
}

/** Factored out so the nav-graph DSL stays readable. */
private fun NavGraphBuilder.mainGraph(
    onPlay: (Track, List<Track>) -> Unit,
    onOpenSearch: () -> Unit,
    onBack: () -> Unit,
) {
    composable(Route.Home.path) {
        HomeScreen(onTrackClick = onPlay, onOpenSearch = onOpenSearch)
    }
    composable(Route.Search.path) {
        SearchScreen(onTrackClick = onPlay, onBack = onBack)
    }
    composable(Route.Library.path) {
        LibraryScreen(onTrackClick = onPlay)
    }
    composable(Route.Profile.path) {
        ProfileScreen(onTrackClick = onPlay)
    }
    composable(Route.Player.path) {
        PlayerScreen(onBack = onBack)
    }
}
