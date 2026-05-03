package com.vibewave.ui.screens.library

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LibraryMusic
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vibewave.R
import com.vibewave.domain.model.Track
import com.vibewave.ui.components.TrackRow
import com.vibewave.ui.screens.player.PlayerViewModel
import com.vibewave.ui.theme.VibeWaveTheme

/**
 * Library screen — local audio files from /Music and /Download.
 *
 * Permission flow:
 *   1. On first composition, we check if READ_MEDIA_AUDIO (API 33+) or
 *      READ_EXTERNAL_STORAGE (API ≤32) is already granted. If yes — load.
 *   2. Otherwise we launch the system permission dialog.
 *   3. If user accepts → load tracks. If user denies → show "no access" state.
 */
@Composable
fun LibraryScreen(
    onTrackClick: (Track, List<Track>) -> Unit,
    vm: LibraryViewModel = hiltViewModel(),
    playerVm: PlayerViewModel = hiltViewModel(),
) {
    val colors = VibeWaveTheme.colors
    val ctx = LocalContext.current
    val state by vm.state.collectAsStateWithLifecycle()
    val nowPlaying by playerVm.currentTrack.collectAsStateWithLifecycle()
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        Manifest.permission.READ_MEDIA_AUDIO
    else
        Manifest.permission.READ_EXTERNAL_STORAGE

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) vm.onPermissionGranted() else vm.onPermissionDenied()
    }

    // On first launch: check permission and either load or request
    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(ctx, permission) ==
            PackageManager.PERMISSION_GRANTED
        if (granted) {
            vm.onPermissionGranted()
        } else {
            permissionLauncher.launch(permission)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = topInset),
    ) {
        // Header
        Text(
            text = stringResource(R.string.library_title),
            color = colors.onSurface,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
        )

        when (val s = state) {
            is LibraryUiState.Idle, is LibraryUiState.Loading -> LoadingState()
            is LibraryUiState.PermissionDenied -> PermissionDeniedState(
                onRetry = { permissionLauncher.launch(permission) }
            )
            is LibraryUiState.Ready -> {
                if (s.tracks.isEmpty()) {
                    EmptyState()
                } else {
                    TrackList(
                        tracks = s.tracks,
                        nowPlayingId = nowPlaying?.id,
                        onClick = onTrackClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = VibeWaveTheme.colors.accent)
    }
}

@Composable
private fun PermissionDeniedState(onRetry: () -> Unit) {
    val colors = VibeWaveTheme.colors
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Rounded.LibraryMusic,
            contentDescription = null,
            tint = colors.onSurfaceMuted,
            modifier = Modifier.size(64.dp),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.library_permission_denied),
            color = colors.onSurface,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.library_permission_explanation),
            color = colors.onSurfaceMuted,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(colors.accent)
                .clickable(onClick = onRetry)
                .padding(horizontal = 24.dp, vertical = 12.dp),
        ) {
            Text(
                text = stringResource(R.string.library_grant_permission),
                color = androidx.compose.ui.graphics.Color.Black,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}

@Composable
private fun EmptyState() {
    val colors = VibeWaveTheme.colors
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Rounded.LibraryMusic,
            contentDescription = null,
            tint = colors.onSurfaceMuted,
            modifier = Modifier.size(64.dp),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.library_empty_title),
            color = colors.onSurface,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.library_empty_subtitle),
            color = colors.onSurfaceMuted,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun TrackList(
    tracks: List<Track>,
    nowPlayingId: Long?,
    onClick: (Track, List<Track>) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(
            start = 8.dp,
            end = 8.dp,
            top = 4.dp,
            // bottom padding leaves room for mini-player + bottom bar
            bottom = 200.dp,
        ),
    ) {
        items(tracks, key = { it.id }) { track ->
            TrackRow(
                track = track,
                isPlaying = nowPlayingId == track.id,
                onClick = { onClick(track, tracks) },
            )
        }
    }
}
