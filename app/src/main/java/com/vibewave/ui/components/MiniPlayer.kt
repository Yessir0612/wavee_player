package com.vibewave.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vibewave.domain.model.Track
import com.vibewave.ui.theme.VibeWaveTheme
import dev.chrisbanes.haze.HazeState

/**
 * Mini-player bar.
 *
 * **Background** = the current track's album art, blurred with [Modifier.blur]
 * + a dark scrim so text stays readable. Self-contained — does not depend
 * on any shared [HazeState] for the album-art blur (we keep hazeState in
 * the signature for future use but don't require it to work).
 *
 * Tap the whole bar → opens full player.
 * Tap play/pause or next → doesn't bubble up.
 */
@Composable
fun MiniPlayer(
    hazeState: HazeState,
    track: Track?,
    isPlaying: Boolean,
    progress: Float,                 // 0f..1f
    onClick: () -> Unit,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = VibeWaveTheme.colors
    if (track == null) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(colors.surfaceElevated)      // fallback if art missing
                .clickable(onClick = onClick),
        ) {
            // ── Blurred album art background ─────────────────────────────
            Box {
                AsyncImage(
                    model = track.albumArt,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .matchParentSize()
                        .blur(radius = 30.dp),
                )
                // Scrim so text has contrast over any kind of artwork
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color.Black.copy(alpha = 0.55f),
                                    Color.Black.copy(alpha = 0.45f),
                                )
                            )
                        )
                )

                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Crisp album thumbnail (not blurred)
                    AsyncImage(
                        model = track.albumArt,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(colors.surface),
                    )
                    Spacer(Modifier.width(12.dp))

                    Column(Modifier.weight(1f)) {
                        Text(
                            track.title,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            track.artist,
                            color = Color.White.copy(alpha = 0.75f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }

                    IconButton(onClick = onPlayPause) {
                        Crossfade(targetState = isPlaying, label = "pp") { playing ->
                            Icon(
                                imageVector = if (playing) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                                contentDescription = if (playing) "Pause" else "Play",
                                tint = Color.White,
                            )
                        }
                    }
                    IconButton(onClick = onNext) {
                        Icon(
                            Icons.Rounded.SkipNext,
                            contentDescription = "Next",
                            tint = Color.White,
                        )
                    }
                }
            }

            // ── Thin progress line at the very bottom ────────────────────
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                color = colors.accent,
                trackColor = Color.White.copy(alpha = 0.12f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
            )
        }
    }
}
