package com.vibewave.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.vibewave.data.repository.AppearanceSettings

/**
 * A custom theme layer built on top of MaterialTheme.
 *
 * Why not just use MaterialTheme directly? Because we need:
 *   • a gradient background ([Brush]) — Material 3 only has flat colors
 *   • per-screen accent color independent of the primary theme
 *   • quick access to a "muted" on-surface color for captions
 *
 * These are exposed through [LocalVibeColors] so any composable can read
 * them with `VibeWaveTheme.colors`.
 */
data class VibeColors(
    val background: Brush,
    val surface: Color,
    val surfaceElevated: Color,
    val onBackground: Color,
    val onSurface: Color,
    val onSurfaceMuted: Color,
    val accent: Color,
)

val LocalVibeColors = compositionLocalOf<VibeColors> {
    error("VibeColors not provided — wrap your content in VibeWaveTheme")
}

object VibeWaveTheme {
    val colors: VibeColors
        @Composable @ReadOnlyComposable get() = LocalVibeColors.current
}

@Composable
fun VibeWaveTheme(
    settings: AppearanceSettings,
    dynamicAccent: Color? = null,          // optional — extracted from album art
    content: @Composable () -> Unit,
) {
    val scheme = PaletteColors.schemeFor(settings.palette)
    val accent = dynamicAccent ?: Color(settings.accent.argb)
    val isLightTheme = settings.palette == com.vibewave.data.datastore.ThemePalette.WHITE

    val colors = VibeColors(
        background = scheme.background,
        surface = scheme.surface,
        surfaceElevated = scheme.surfaceElevated,
        onBackground = scheme.onBackground,
        onSurface = scheme.onSurface,
        onSurfaceMuted = scheme.onSurfaceMuted,
        accent = accent,
    )

    // Bridge to Material 3 so ripples, TextField etc. still look right.
    val materialScheme = if (isLightTheme) {
        androidx.compose.material3.lightColorScheme(
            primary = accent,
            onPrimary = Color.White,
            secondary = accent,
            background = scheme.surface,
            onBackground = scheme.onSurface,
            surface = scheme.surface,
            onSurface = scheme.onSurface,
            surfaceVariant = scheme.surfaceElevated,
        )
    } else {
        darkColorScheme(
            primary = accent,
            onPrimary = Color.Black,
            secondary = accent,
            background = scheme.surface,
            onBackground = scheme.onSurface,
            surface = scheme.surface,
            onSurface = scheme.onSurface,
            surfaceVariant = scheme.surfaceElevated,
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        androidx.compose.runtime.SideEffect {
            val window = (view.context as android.app.Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isLightTheme
        }
    }

    CompositionLocalProvider(LocalVibeColors provides colors) {
        MaterialTheme(
            colorScheme = materialScheme,
            typography = buildTypography(settings.font.toFamily()),
            content = content,
        )
    }
}
