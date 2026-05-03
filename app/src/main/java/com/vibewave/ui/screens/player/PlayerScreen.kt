package com.vibewave.ui.screens.player

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.QueueMusic
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Lyrics
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.RepeatOne
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material.icons.rounded.VolumeDown
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import coil.compose.AsyncImage
import com.vibewave.R
import com.vibewave.ui.components.WaveformBar
import com.vibewave.ui.theme.VibeWaveTheme

/**
 * Fullscreen player.
 *
 * Layout (top → bottom):
 *   [1] Full-screen blurred album art + dark scrim (only on this screen)
 *   [2] Header (close, album name, ⋮)
 *   [3] Large CIRCULAR album art, slowly rotating while playing
 *   [4] Track title + artist
 *   [5] Waveform seek bar + time labels
 *   [6] Main controls row (shuffle, prev, play/pause, next, repeat)
 *   [7] Service buttons row (volume slider, queue, lyrics, favorite)
 *
 * The blurred album art is painted by this composable — not shared with
 * any other screen — so when the player closes, it closes cleanly and
 * doesn't leave the rest of the app tinted.
 */
@Composable
fun PlayerScreen(
    onBack: () -> Unit,
    vm: PlayerViewModel = hiltViewModel(),
) {
    val colors = VibeWaveTheme.colors
    val track by vm.currentTrack.collectAsStateWithLifecycle()
    val isPlaying by vm.isPlaying.collectAsStateWithLifecycle()
    val progressMs by vm.progressMs.collectAsStateWithLifecycle()
    val durationMs by vm.durationMs.collectAsStateWithLifecycle()
    val progressFrac by vm.progressFraction.collectAsStateWithLifecycle()
    val shuffle by vm.shuffle.collectAsStateWithLifecycle()
    val repeat by vm.repeatMode.collectAsStateWithLifecycle()
    val isFavorite by vm.isCurrentFavorite.collectAsStateWithLifecycle()

    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    if (track == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.player_no_track), color = colors.onSurfaceMuted)
        }
        return
    }
    val t = track!!

    // Local volume state (0..1). Real audio volume is controlled by the
    // device; this slider is visual-only for now but would hook into
    // a future AudioManager wiring.
    var volume by remember { mutableFloatStateOf(0.75f) }

    Box(modifier = Modifier.fillMaxSize().background(colors.background)) {

        // ── [1] Blurred album-art background (player-local) ─────────────
        AsyncImage(
            model = t.albumArt,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .blur(radius = 60.dp),
            alpha = 0.5f,
        )
        // Darkening scrim for text contrast over any artwork
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Black.copy(alpha = 0.35f),
                        0.5f to Color.Black.copy(alpha = 0.5f),
                        1f to Color.Black.copy(alpha = 0.8f),
                    )
                )
        )

        // ── Foreground content ──────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topInset, bottom = bottomInset)
                .padding(horizontal = 24.dp),
        ) {
            // [2] Header
            Row(
                modifier = Modifier.fillMaxWidth().height(56.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.Rounded.KeyboardArrowDown,
                        stringResource(R.string.player_close),
                        tint = Color.White,
                    )
                }
                Text(
                    text = t.albumTitle.uppercase(),
                    color = Color.White.copy(alpha = 0.75f),
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                    textAlign = TextAlign.Center,
                )
                IconButton(onClick = { /* more */ }) {
                    Icon(Icons.Rounded.MoreVert, stringResource(R.string.player_more), tint = Color.White.copy(alpha = 0.75f))
                }
            }

            Spacer(Modifier.height(16.dp))

            // [3] CIRCULAR album art — rotates while playing
            val rotation = rememberInfiniteRotation(enabled = isPlaying)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 340.dp)
                    .aspectRatio(1f)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    model = t.albumArt,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .rotate(rotation),
                )
                // Center "vinyl hole" — a tiny dark dot gives the
                // rotation something to visually pivot around
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.85f)),
                )
            }

            Spacer(Modifier.height(20.dp))

            // [4] Title + artist
            Text(
                t.title,
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(4.dp))
            Text(
                t.artist,
                color = Color.White.copy(alpha = 0.65f),
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(16.dp))

            // [5] Waveform seek bar + times
            WaveformBar(
                progress = progressFrac,
                onSeek = vm::seekFraction,
                seed = (t.id % 10_000).toInt(),
                modifier = Modifier.fillMaxWidth(),
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    formatTime(progressMs),
                    color = Color.White.copy(alpha = 0.65f),
                    style = MaterialTheme.typography.labelSmall,
                )
                Text(
                    "-${formatTime((durationMs - progressMs).coerceAtLeast(0))}",
                    color = Color.White.copy(alpha = 0.65f),
                    style = MaterialTheme.typography.labelSmall,
                )
            }

            Spacer(Modifier.height(20.dp))

            // [6] Main controls — shuffle, prev, PLAY, next, repeat
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = vm::toggleShuffle) {
                    Icon(
                        Icons.Rounded.Shuffle,
                        stringResource(R.string.player_shuffle),
                        tint = if (shuffle) colors.accent else Color.White.copy(alpha = 0.75f),
                        modifier = Modifier.size(24.dp),
                    )
                }
                IconButton(onClick = vm::prev) {
                    Icon(
                        Icons.Rounded.SkipPrevious,
                        stringResource(R.string.player_prev),
                        tint = Color.White,
                        modifier = Modifier.size(36.dp),
                    )
                }
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(colors.accent),
                    contentAlignment = Alignment.Center,
                ) {
                    IconButton(onClick = vm::toggle, modifier = Modifier.size(72.dp)) {
                        Crossfade(targetState = isPlaying, label = "pp") { playing ->
                            Icon(
                                imageVector = if (playing) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                                contentDescription = if (playing) "Pause" else stringResource(R.string.player_play),
                                tint = Color.Black,
                                modifier = Modifier.size(36.dp),
                            )
                        }
                    }
                }
                IconButton(onClick = vm::next) {
                    Icon(
                        Icons.Rounded.SkipNext,
                        stringResource(R.string.player_next),
                        tint = Color.White,
                        modifier = Modifier.size(36.dp),
                    )
                }
                IconButton(onClick = vm::cycleRepeat) {
                    Icon(
                        imageVector = when (repeat) {
                            Player.REPEAT_MODE_ONE -> Icons.Rounded.RepeatOne
                            else -> Icons.Rounded.Repeat
                        },
                        contentDescription = stringResource(R.string.player_repeat),
                        tint = if (repeat != Player.REPEAT_MODE_OFF) colors.accent
                               else Color.White.copy(alpha = 0.75f),
                        modifier = Modifier.size(24.dp),
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // [7] Service buttons row — volume slider + queue + lyrics + heart
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Volume slider (takes most of the row)
                Icon(
                    Icons.Rounded.VolumeDown,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.75f),
                    modifier = Modifier.size(20.dp),
                )
                Slider(
                    value = volume,
                    onValueChange = { volume = it },
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color.White,
                        inactiveTrackColor = Color.White.copy(alpha = 0.25f),
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 6.dp),
                )
                Icon(
                    Icons.Rounded.VolumeUp,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.75f),
                    modifier = Modifier.size(20.dp),
                )

                Spacer(Modifier.width(12.dp))

                IconButton(onClick = { /* queue */ }) {
                    Icon(
                        Icons.AutoMirrored.Rounded.QueueMusic,
                        contentDescription = stringResource(R.string.player_queue),
                        tint = Color.White.copy(alpha = 0.75f),
                    )
                }
                IconButton(onClick = { /* lyrics */ }) {
                    Icon(
                        Icons.Rounded.Lyrics,
                        contentDescription = stringResource(R.string.player_lyrics),
                        tint = Color.White.copy(alpha = 0.75f),
                    )
                }
                IconButton(onClick = vm::toggleFavorite) {
                    Crossfade(targetState = isFavorite, label = "fav") { liked ->
                        Icon(
                            imageVector = if (liked) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            contentDescription = if (liked) "Unlike" else "Like",
                            tint = if (liked) colors.accent else Color.White.copy(alpha = 0.75f),
                        )
                    }
                }
            }
        }
    }
}

/**
 * Slow-rotate a value 0..360 while [enabled] is true. Paused when
 * [enabled] is false — the rotation freezes at its current angle.
 */
@Composable
private fun rememberInfiniteRotation(enabled: Boolean): Float {
    if (!enabled) return 0f
    val transition = rememberInfiniteTransition(label = "rotation")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20_000, easing = LinearEasing),
        ),
        label = "albumRotation",
    )
    return rotation
}

/** Format `ms` as `m:ss`. */
private fun formatTime(ms: Long): String {
    val totalSec = (ms / 1000).coerceAtLeast(0)
    val m = totalSec / 60
    val s = totalSec % 60
    return "$m:${s.toString().padStart(2, '0')}"
}
