package com.vibewave.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.vibewave.data.datastore.ThemePalette

/**
 * Each palette defines its own atmospheric gradient background.
 *
 * We use vertical [Brush.verticalGradient] so the top tint is darker and the
 * bottom slightly lifts — gives a cinematic, depth-y feel instead of flat black.
 */
object PaletteColors {

    data class Scheme(
        val background: Brush,
        val surface: Color,
        val surfaceElevated: Color,
        val onBackground: Color,
        val onSurface: Color,
        val onSurfaceMuted: Color,
    )

    fun schemeFor(palette: ThemePalette): Scheme = when (palette) {
        ThemePalette.OBSIDIAN -> Scheme(
            background = Brush.verticalGradient(listOf(Color(0xFF0E0E12), Color(0xFF1A1A24))),
            surface = Color(0xFF15151C),
            surfaceElevated = Color(0xFF1F1F2A),
            onBackground = Color.White,
            onSurface = Color.White,
            onSurfaceMuted = Color(0xFFB0B0B8),
        )

        ThemePalette.AMOLED -> Scheme(
            background = Brush.verticalGradient(listOf(Color.Black, Color.Black)),
            surface = Color(0xFF0A0A0A),
            surfaceElevated = Color(0xFF111111),
            onBackground = Color.White,
            onSurface = Color.White,
            onSurfaceMuted = Color(0xFF8A8A8A),
        )

        ThemePalette.MIDNIGHT -> Scheme(
            background = Brush.verticalGradient(listOf(Color(0xFF0B1026), Color(0xFF1A1B3A))),
            surface = Color(0xFF141833),
            surfaceElevated = Color(0xFF1E2345),
            onBackground = Color.White,
            onSurface = Color.White,
            onSurfaceMuted = Color(0xFFA8ADCC),
        )

        ThemePalette.EMERALD -> Scheme(
            background = Brush.verticalGradient(listOf(Color(0xFF051B12), Color(0xFF0D2A1C))),
            surface = Color(0xFF0F2419),
            surfaceElevated = Color(0xFF173024),
            onBackground = Color.White,
            onSurface = Color.White,
            onSurfaceMuted = Color(0xFF9DB8A8),
        )

        ThemePalette.SUNSET -> Scheme(
            background = Brush.verticalGradient(listOf(Color(0xFF1A0A08), Color(0xFF3A1208))),
            surface = Color(0xFF200C09),
            surfaceElevated = Color(0xFF2B1510),
            onBackground = Color.White,
            onSurface = Color.White,
            onSurfaceMuted = Color(0xFFD6A89A),
        )

        ThemePalette.ROSE -> Scheme(
            background = Brush.verticalGradient(listOf(Color(0xFF1A0A14), Color(0xFF2D1221))),
            surface = Color(0xFF1E0C18),
            surfaceElevated = Color(0xFF29121F),
            onBackground = Color.White,
            onSurface = Color.White,
            onSurfaceMuted = Color(0xFFD9ADC2),
        )

        ThemePalette.CYBER -> Scheme(
            background = Brush.verticalGradient(listOf(Color(0xFF050510), Color(0xFF0A0A1E))),
            surface = Color(0xFF0A0A1A),
            surfaceElevated = Color(0xFF121228),
            onBackground = Color(0xFFE0FFE0),
            onSurface = Color(0xFFE0FFE0),
            onSurfaceMuted = Color(0xFF80C080),
        )

        ThemePalette.OCEAN -> Scheme(
            background = Brush.verticalGradient(listOf(Color(0xFF061522), Color(0xFF0E2538))),
            surface = Color(0xFF0B1E2E),
            surfaceElevated = Color(0xFF13293D),
            onBackground = Color.White,
            onSurface = Color.White,
            onSurfaceMuted = Color(0xFF9CC5DB),
        )

        ThemePalette.WHITE -> Scheme(
            background = Brush.verticalGradient(listOf(Color(0xFFF5F5F7), Color(0xFFEBEBF0))),
            surface = Color(0xFFFFFFFF),
            surfaceElevated = Color(0xFFF0F0F5),
            onBackground = Color(0xFF111111),
            onSurface = Color(0xFF111111),
            onSurfaceMuted = Color(0xFF888899),
        )
    }
}
