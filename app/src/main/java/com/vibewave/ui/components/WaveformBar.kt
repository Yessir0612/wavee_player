package com.vibewave.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import com.vibewave.ui.theme.VibeWaveTheme
import kotlin.math.absoluteValue
import kotlin.math.sin
import kotlin.random.Random

/**
 * A waveform-style seek bar.
 *
 * Each bar is a short vertical rectangle. Bars to the left of the
 * playhead are painted in the accent color; bars to the right are
 * muted white. The bar exactly at the playhead is slightly taller
 * for a subtle "playhead" effect.
 *
 * The bar heights are deterministic (seeded pseudo-random) so the
 * pattern stays stable between recompositions and feels like a real
 * waveform rather than noise.
 *
 * Tapping anywhere on the bar seeks to that position via [onSeek].
 */
@Composable
fun WaveformBar(
    progress: Float,                              // 0f..1f
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
    barCount: Int = 56,
    height: Dp = 40.dp,
    seed: Int = 0,
) {
    val colors = VibeWaveTheme.colors

    // Smooth the progress so seeking animates, not jumps.
    val smoothProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 120),
        label = "waveformProgress",
    )

    // Deterministic waveform — each bar has a pseudo-random "amplitude"
    // between 0.2 and 1.0. We use a fast mixer so different seed values
    // produce different patterns (e.g., seed by track id in caller).
    val amplitudes = remember(barCount, seed) {
        FloatArray(barCount) { i ->
            val noise = sin(i * 0.6f + seed * 0.41f).absoluteValue
            val jitter = Random(seed * 1000 + i).nextFloat()
            (0.25f + 0.55f * noise + 0.2f * jitter).coerceIn(0.2f, 1f)
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val fraction = (offset.x / size.width).coerceIn(0f, 1f)
                    onSeek(fraction)
                }
            }
    ) {
        val w = size.width
        val h = size.height
        val gap = 2.dp.toPx()
        val barWidth = ((w - gap * (barCount - 1)) / barCount).coerceAtLeast(1f)

        // Playhead index = which bar the progress is on
        val playheadIndex = (smoothProgress * barCount).toInt().coerceIn(0, barCount - 1)

        for (i in 0 until barCount) {
            val amp = amplitudes[i]
            // Give the playhead bar a ~20% boost so it reads as "current"
            val scaledAmp = if (i == playheadIndex) (amp * 1.2f).coerceAtMost(1f) else amp
            val barH = h * scaledAmp
            val x = i * (barWidth + gap)
            val y = (h - barH) / 2f

            val color = when {
                i < playheadIndex -> colors.accent
                i == playheadIndex -> colors.accent
                else -> Color.White.copy(alpha = 0.22f)
            }

            drawRoundRect(
                color = color,
                topLeft = Offset(x, y),
                size = Size(barWidth, barH),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(barWidth / 2, barWidth / 2),
            )
        }
    }
}
