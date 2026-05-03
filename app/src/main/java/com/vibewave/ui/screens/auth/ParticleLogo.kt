package com.vibewave.ui.screens.auth

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun ParticleLogo(
    particleCount: Int = 380,
    accent: Color,
    onReady: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val particles = remember {
        List(particleCount) {
            Particle(
                startX = Random.nextFloat(),
                startY = Random.nextFloat(),
                targetX = Random.nextFloat(),
                targetY = Random.nextFloat(),
                phase = Random.nextFloat() * 6.283f,
                speed = 0.5f + Random.nextFloat() * 1.5f,
                size = 1.5f + Random.nextFloat() * 2.5f,
            )
        }
    }

    var canvasW by remember { mutableStateOf(0f) }
    var canvasH by remember { mutableStateOf(0f) }

    var convergeTarget by remember { mutableStateOf(0f) }
    val converge by animateFloatAsState(
        targetValue = convergeTarget,
        animationSpec = tween(durationMillis = 1600, easing = LinearEasing),
        label = "converge",
    )

    // Fire animation once canvas size is known via onGloballyPositioned
    LaunchedEffect(canvasW, canvasH) {
        if (canvasW <= 0f || canvasH <= 0f) return@LaunchedEffect
        val targets = sampleLogoPoints(particleCount, canvasW, canvasH)
        particles.forEachIndexed { i, p ->
            val (tx, ty) = targets[i]
            p.targetX = tx
            p.targetY = ty
        }
        kotlinx.coroutines.delay(400)
        convergeTarget = 1f
        kotlinx.coroutines.delay(1800)
        onReady()
    }

    // Guaranteed fallback — buttons appear after 2.8s no matter what
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2800)
        onReady()
    }

    var time by remember { mutableStateOf(0f) }
    LaunchedEffect(Unit) {
        var last = 0L
        while (true) {
            withFrameNanos { now ->
                if (last != 0L) time += ((now - last) / 1e9f)
                last = now
            }
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { coords ->
                val s = coords.size
                if (s.width > 0 && s.height > 0) {
                    canvasW = s.width.toFloat()
                    canvasH = s.height.toFloat()
                }
            },
    ) {
        for (p in particles) {
            val wanderX = p.startX * canvasW + sin(time * p.speed + p.phase) * (canvasW * 0.04f)
            val wanderY = p.startY * canvasH + cos(time * p.speed * 0.7f + p.phase) * (canvasH * 0.04f)

            val jitter = 2f + sin(time * 2.5f + p.phase) * 1.2f
            val tgtX = p.targetX + sin(time * 1.2f + p.phase) * jitter
            val tgtY = p.targetY + cos(time * 1.6f + p.phase) * jitter

            val x = lerp(wanderX, tgtX, converge)
            val y = lerp(wanderY, tgtY, converge)

            val alpha = 0.3f + 0.7f * converge
            drawCircle(
                color = accent.copy(alpha = alpha),
                radius = p.size,
                center = Offset(x, y),
            )
        }
    }
}

private fun lerp(a: Float, b: Float, t: Float): Float = a + (b - a) * t

private class Particle(
    val startX: Float, val startY: Float,
    var targetX: Float, var targetY: Float,
    val phase: Float,
    val speed: Float,
    val size: Float,
)

private fun sampleLogoPoints(count: Int, canvasW: Float, canvasH: Float): List<Pair<Float, Float>> {
    data class Rect(val x: Float, val y: Float, val w: Float, val h: Float)
    val svgW = 329f
    val svgH = 83f
    val glyphs = listOf(
        Rect(0f,   0f,  115f, 82f),
        Rect(102f, 21f, 56f,  62f),
        Rect(152f, 22f, 63f,  60f),
        Rect(207f, 21f, 62f,  62f),
        Rect(267f, 21f, 62f,  62f),
    )
    val logoW = canvasW * 0.72f
    val scale = logoW / svgW
    val offsetX = (canvasW - logoW) / 2f
    val offsetY = canvasH * 0.28f
    val areas = glyphs.map { it.w * it.h }
    val totalArea = areas.sum()
    val perGlyph = areas.map { area -> (count * (area / totalArea)).toInt() }
    val result = mutableListOf<Pair<Float, Float>>()
    glyphs.forEachIndexed { idx, g ->
        val n = perGlyph[idx].coerceAtLeast(1)
        repeat(n) {
            val rx = g.x + Random.nextFloat() * g.w
            val ry = g.y + Random.nextFloat() * g.h
            result.add((offsetX + rx * scale) to (offsetY + ry * scale))
        }
    }
    while (result.size < count) {
        val g = glyphs.random()
        val rx = g.x + Random.nextFloat() * g.w
        val ry = g.y + Random.nextFloat() * g.h
        result.add((offsetX + rx * scale) to (offsetY + ry * scale))
    }
    return result.take(count)
}
