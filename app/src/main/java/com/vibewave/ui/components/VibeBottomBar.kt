package com.vibewave.ui.components

import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.vibewave.ui.navigation.BottomDest
import com.vibewave.ui.navigation.Route
import com.vibewave.ui.theme.VibeWaveTheme
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeChild

@Composable
fun VibeBottomBar(
    hazeState: HazeState,
    destinations: List<BottomDest>,
    currentRoute: String?,
    onClick: (BottomDest) -> Unit,
    avatarUri: String? = null,
    modifier: Modifier = Modifier,
) {
    val colors = VibeWaveTheme.colors
    val navInsets = WindowInsets.navigationBars.asPaddingValues()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) awaitPointerEvent()
                }
            }
            .hazeChild(
                state = hazeState,
                style = HazeStyle(
                    backgroundColor = colors.surface,
                    tint = HazeTint(colors.surface.copy(alpha = 0.88f)),
                    blurRadius = 24.dp,
                ),
            )
            .background(
                Brush.verticalGradient(
                    0f to colors.surface.copy(alpha = 0.65f),
                    0.3f to colors.surface.copy(alpha = 0.9f),
                    1f to colors.surface.copy(alpha = 0.95f),
                )
            )
            .padding(bottom = navInsets.calculateBottomPadding())
            .padding(horizontal = 8.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        destinations.forEach { dest ->
            val selected = currentRoute == dest.route.path
            val bg by animateColorAsState(
                if (selected) colors.accent.copy(alpha = 0.18f) else Color.Transparent,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "bg",
            )
            val tint by animateColorAsState(
                if (selected) colors.accent else colors.onSurfaceMuted,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "tint",
            )
            val isProfile = dest.route == Route.Profile
            val hasAvatar = isProfile && avatarUri != null

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(bg)
                    .clickable { onClick(dest) }
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (hasAvatar) {
                    // Show avatar circle instead of icon
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(colors.accent.copy(alpha = 0.3f)),
                    ) {
                        AsyncImage(
                            model = Uri.parse(avatarUri),
                            contentDescription = dest.label,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape),
                        )
                    }
                } else {
                    Icon(
                        imageVector = dest.icon,
                        contentDescription = dest.label,
                        tint = tint,
                        modifier = Modifier.size(22.dp),
                    )
                }
                if (selected) {
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = dest.label,
                        color = tint,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}

/**
 * Bottom navigation with:
 *  - a fully opaque/blurred backdrop so empty areas don't click-through
 *  - animated pill indicator under the selected item
 *  - a subtle top-gradient so content behind fades into the bar
 *
 * The [hazeState] is the one shared with the NavHost; we blur the content
 * scrolling behind the bar, which looks like frosted glass.
 */
@Composable
fun VibeBottomBar(
    hazeState: HazeState,
    destinations: List<BottomDest>,
    currentRoute: String?,
    onClick: (BottomDest) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = VibeWaveTheme.colors
    val navInsets = WindowInsets.navigationBars.asPaddingValues()

    // Soak up any touches that don't land on a specific item so they can
    // never reach the content behind us (tracks / buttons on Home/Search).
    Row(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                // Swallow every pointer event. Children's clickable{} handles
                // its own taps first; anything that falls through dies here.
                awaitPointerEventScope {
                    while (true) awaitPointerEvent()
                }
            }
            .hazeChild(
                state = hazeState,
                style = HazeStyle(
                    backgroundColor = colors.surface,
                    tint = HazeTint(colors.surface.copy(alpha = 0.88f)),
                    blurRadius = 24.dp,
                ),
            )
            .background(
                // Additional gradient fade at the top edge for a clean seam
                // between content and the bar
                Brush.verticalGradient(
                    0f to colors.surface.copy(alpha = 0.65f),
                    0.3f to colors.surface.copy(alpha = 0.9f),
                    1f to colors.surface.copy(alpha = 0.95f),
                )
            )
            .padding(bottom = navInsets.calculateBottomPadding())
            .padding(horizontal = 8.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        destinations.forEach { dest ->
            val selected = currentRoute == dest.route.path
            val bg by animateColorAsState(
                if (selected) colors.accent.copy(alpha = 0.18f) else Color.Transparent,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "bg",
            )
            val tint by animateColorAsState(
                if (selected) colors.accent else colors.onSurfaceMuted,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "tint",
            )

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(bg)
                    .clickable { onClick(dest) }
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = dest.icon,
                    contentDescription = dest.label,
                    tint = tint,
                    modifier = Modifier.size(22.dp),
                )
                if (selected) {
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = dest.label,
                        color = tint,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}
