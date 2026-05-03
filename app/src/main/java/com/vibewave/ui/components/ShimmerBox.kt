package com.vibewave.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vibewave.ui.theme.VibeWaveTheme

/**
 * A shimmer placeholder that animates a soft diagonal gradient.
 * Used for loading skeletons on the search and home screens.
 */
@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    cornerRadius: Int = 8,
) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val x by transition.animateFloat(
        initialValue = -400f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(tween(1400, easing = LinearEasing)),
        label = "x",
    )

    val colors = VibeWaveTheme.colors
    val brush = Brush.linearGradient(
        colors = listOf(
            colors.surface,
            colors.surfaceElevated,
            colors.surface,
        ),
        start = androidx.compose.ui.geometry.Offset(x, 0f),
        end = androidx.compose.ui.geometry.Offset(x + 400f, 400f),
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius.dp))
            .background(brush),
    )
}

/** A placeholder that mimics a [com.vibewave.ui.components.TrackRow]. */
@Composable
fun ShimmerTrackRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ShimmerBox(Modifier.size(50.dp), cornerRadius = 8)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            ShimmerBox(Modifier.width(180.dp).height(14.dp), cornerRadius = 4)
            Spacer(Modifier.height(6.dp))
            ShimmerBox(Modifier.width(120.dp).height(11.dp), cornerRadius = 4)
        }
        ShimmerBox(Modifier.width(40.dp).height(11.dp), cornerRadius = 4)
    }
}
