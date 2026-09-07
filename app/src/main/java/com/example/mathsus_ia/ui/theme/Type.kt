package com.example.mathsus_ia.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.jesusgurrute.mathsus_ia.R

val OverPassFontFamily = FontFamily(
    Font(R.font.overpass_black, FontWeight.Black),
    Font(R.font.overpass_bold, FontWeight.Bold),
    Font(R.font.overpass_extrabold, FontWeight.ExtraBold),
    Font(R.font.overpass_extralight, FontWeight.ExtraLight),
    Font(R.font.overpass_light, FontWeight.Light),
    Font(R.font.overpass_regular, FontWeight.Normal),
    Font(R.font.overpass_semibold, FontWeight.SemiBold),
    Font(R.font.overpass_thin, FontWeight.Thin),
)

/**
 * Una sola familia tipográfica (Overpass) en toda la app, con la escala
 * completa de Material 3 — antes solo se sobreescribía bodyLarge y el resto
 * caía a la fuente por defecto del sistema, de ahí la mezcla de tipografías.
 */
val Typography = Typography(
    displayLarge = TextStyle(fontFamily = OverPassFontFamily, fontWeight = FontWeight.Bold, fontSize = 52.sp, lineHeight = 58.sp, letterSpacing = (-0.25).sp),
    displayMedium = TextStyle(fontFamily = OverPassFontFamily, fontWeight = FontWeight.Bold, fontSize = 40.sp, lineHeight = 46.sp, letterSpacing = 0.sp),
    displaySmall = TextStyle(fontFamily = OverPassFontFamily, fontWeight = FontWeight.Bold, fontSize = 32.sp, lineHeight = 40.sp, letterSpacing = 0.sp),

    headlineLarge = TextStyle(fontFamily = OverPassFontFamily, fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 34.sp, letterSpacing = 0.sp),
    headlineMedium = TextStyle(fontFamily = OverPassFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 24.sp, lineHeight = 30.sp, letterSpacing = 0.sp),
    headlineSmall = TextStyle(fontFamily = OverPassFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 26.sp, letterSpacing = 0.sp),

    titleLarge = TextStyle(fontFamily = OverPassFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 26.sp, letterSpacing = 0.sp),
    titleMedium = TextStyle(fontFamily = OverPassFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 22.sp, letterSpacing = 0.1.sp),
    titleSmall = TextStyle(fontFamily = OverPassFontFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp),

    bodyLarge = TextStyle(fontFamily = OverPassFontFamily, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.15.sp),
    bodyMedium = TextStyle(fontFamily = OverPassFontFamily, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.15.sp),
    bodySmall = TextStyle(fontFamily = OverPassFontFamily, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 18.sp, letterSpacing = 0.2.sp),

    labelLarge = TextStyle(fontFamily = OverPassFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp),
    labelMedium = TextStyle(fontFamily = OverPassFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp),
    labelSmall = TextStyle(fontFamily = OverPassFontFamily, fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp),
)
