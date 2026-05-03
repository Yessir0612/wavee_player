package com.vibewave.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.vibewave.R
import com.vibewave.data.datastore.AppFont

private val ttHovesPro = FontFamily(
    Font(R.font.tt_hoves_pro_regular,  FontWeight.Normal),
    Font(R.font.tt_hoves_pro_medium,   FontWeight.Medium),
    Font(R.font.tt_hoves_pro_demibold, FontWeight.SemiBold),
    Font(R.font.tt_hoves_pro_bold,     FontWeight.Bold),
)

private val interTight = FontFamily(
    Font(R.font.inter_tight_regular,  FontWeight.Normal),
    Font(R.font.inter_tight_medium,   FontWeight.Medium),
    Font(R.font.inter_tight_semibold, FontWeight.SemiBold),
    Font(R.font.inter_tight_bold,     FontWeight.Bold),
)

fun AppFont.toFamily(): FontFamily = when (this) {
    AppFont.TT_HOVES    -> ttHovesPro
    AppFont.INTER_TIGHT -> interTight
}

fun buildTypography(family: FontFamily): Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = family, fontWeight = FontWeight.Bold,
        fontSize = 40.sp, lineHeight = 48.sp, letterSpacing = (-0.5).sp,
    ),
    displayMedium = TextStyle(
        fontFamily = family, fontWeight = FontWeight.Bold,
        fontSize = 32.sp, lineHeight = 40.sp, letterSpacing = (-0.3).sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = family, fontWeight = FontWeight.Bold,
        fontSize = 28.sp, lineHeight = 34.sp, letterSpacing = (-0.2).sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = family, fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp, lineHeight = 28.sp, letterSpacing = (-0.1).sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = family, fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp, lineHeight = 24.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = family, fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp, lineHeight = 22.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = family, fontWeight = FontWeight.Medium,
        fontSize = 15.sp, lineHeight = 20.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = family, fontWeight = FontWeight.Medium,
        fontSize = 13.sp, lineHeight = 18.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = family, fontWeight = FontWeight.Normal,
        fontSize = 15.sp, lineHeight = 22.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = family, fontWeight = FontWeight.Normal,
        fontSize = 13.sp, lineHeight = 18.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = family, fontWeight = FontWeight.Normal,
        fontSize = 11.sp, lineHeight = 16.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = family, fontWeight = FontWeight.Medium,
        fontSize = 13.sp, lineHeight = 16.sp, letterSpacing = 0.1.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = family, fontWeight = FontWeight.Medium,
        fontSize = 11.sp, lineHeight = 14.sp, letterSpacing = 0.15.sp,
    ),
)
