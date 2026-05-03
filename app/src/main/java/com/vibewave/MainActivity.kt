package com.vibewave

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vibewave.player.PlayerController
import com.vibewave.ui.VibeWaveApp
import com.vibewave.ui.theme.VibeWaveTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

/**
 * Single activity — classic Compose architecture. All screens are
 * composables inside the nav host ([VibeWaveApp]).
 *
 * Connects the [PlayerController] to the [PlaybackService] in `onStart`
 * and releases it in `onStop`. This avoids leaking the session binder.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var player: PlayerController

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent { Root() }
    }

    override fun onStart() {
        super.onStart()
        player.connect()
    }

    override fun onStop() {
        super.onStop()
        // Keep it alive while app is in foreground; release when Activity
        // is actually destroyed.
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}

/**
 * Collects appearance settings and wraps the entire app in the chosen theme
 * and locale. Both theme and language switch fully reactively — no
 * `recreate()` required.
 */
@Composable
private fun Root() {
    val themeVm: AppRootViewModel = hiltViewModel()
    val settings by themeVm.settings.collectAsStateWithLifecycle()

    settings?.let { s ->
        // Override the locale based on the user's saved language preference.
        // We override LocalConfiguration so that stringResource() picks the
        // right locale immediately when the user toggles the switch in Profile.
        // NOTE: We deliberately do NOT override LocalContext here — replacing it
        // with a plain ContextImpl breaks hiltViewModel() which needs an Activity
        // context to create the ViewModelFactory.
        val baseConfig = LocalConfiguration.current
        val localizedConfig = remember(s.language) {
            Configuration(baseConfig).apply { setLocale(Locale(s.language)) }
        }

        CompositionLocalProvider(
            LocalConfiguration provides localizedConfig,
        ) {
            VibeWaveTheme(settings = s) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Transparent,
                ) {
                    VibeWaveApp()
                }
            }
        }
    }
}
