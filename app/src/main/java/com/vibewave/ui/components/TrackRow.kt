package com.vibewave.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.vibewave.domain.model.Track
import com.vibewave.ui.theme.VibeWaveTheme

/**
 * A single row in any track list.
 *
 * Tap the row to play the track, tap the heart to like/unlike.
 *
 * The heart state is observed via a tiny ViewModel ([TrackRowViewModel])
 * that's created per-row — this keeps each row's liked state independent
 * without polluting the parent screen's view model with a Flow per track.
 */
@Composable
fun TrackRow(
    track: Track,
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    heartVm: TrackRowViewModel = hiltViewModel(key = "trackrow_${track.id}"),
) {
    val colors = VibeWaveTheme.colors
    val titleColor by animateColorAsState(
        if (isPlaying) colors.accent else colors.onSurface,
        label = "titleColor",
    )

    // Observe this row's like state. Collected as a Flow so flipping it
    // from anywhere else (player screen, profile) updates the row instantly.
    val liked by heartVm.isFavorite(track.id).collectAsStateWithLifecycle(initialValue = false)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colors.surface)
            .clickable(onClick = onClick)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // ── Album art ────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(colors.surfaceElevated),
            contentAlignment = Alignment.Center,
        ) {
            if (track.albumArt != null) {
                AsyncImage(
                    model = track.albumArt,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                )
            } else {
                Icon(
                    Icons.Rounded.MusicNote,
                    contentDescription = null,
                    tint = colors.onSurfaceMuted,
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        // ── Title / artist ───────────────────────────────────────────────
        Column(modifier = Modifier.weight(1f)) {
            Text(
                track.title,
                color = titleColor,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                track.artist,
                color = colors.onSurfaceMuted,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        // ── Duration + heart ─────────────────────────────────────────────
        Text(
            track.durationFormatted,
            color = colors.onSurfaceMuted,
            style = MaterialTheme.typography.labelMedium,
        )
        IconButton(onClick = { heartVm.toggle(track) }) {
            Crossfade(targetState = liked, label = "fav-${track.id}") { isLiked ->
                Icon(
                    imageVector = if (isLiked) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                    contentDescription = if (isLiked) "Unlike" else "Like",
                    tint = if (isLiked) colors.accent else colors.onSurfaceMuted,
                )
            }
        }
    }
}
