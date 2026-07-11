package com.doodle.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.doodle.app.data.settings.FontSize

fun getTypography(
    fontFamily: com.doodle.app.data.settings.FontFamily,
    fontSize: FontSize
): Typography {
    val typeface = when (fontFamily) {
        com.doodle.app.data.settings.FontFamily.Roboto -> FontFamily.Default
        com.doodle.app.data.settings.FontFamily.Inter -> FontFamily.SansSerif
        com.doodle.app.data.settings.FontFamily.Poppins -> FontFamily.SansSerif
        com.doodle.app.data.settings.FontFamily.Nunito -> FontFamily.SansSerif
        com.doodle.app.data.settings.FontFamily.OpenSans -> FontFamily.SansSerif
    }

    val scale = when (fontSize) {
        FontSize.Small -> 0.85f
        FontSize.Medium -> 1.0f
        FontSize.Large -> 1.15f
        FontSize.ExtraLarge -> 1.3f
    }

    return Typography(
        displayLarge = TextStyle(
            fontFamily = typeface,
            fontWeight = FontWeight.Bold,
            fontSize = (57 * scale).sp,
            lineHeight = (64 * scale).sp
        ),
        displayMedium = TextStyle(
            fontFamily = typeface,
            fontWeight = FontWeight.Bold,
            fontSize = (45 * scale).sp,
            lineHeight = (52 * scale).sp
        ),
        displaySmall = TextStyle(
            fontFamily = typeface,
            fontWeight = FontWeight.Bold,
            fontSize = (36 * scale).sp,
            lineHeight = (44 * scale).sp
        ),
        headlineLarge = TextStyle(
            fontFamily = typeface,
            fontWeight = FontWeight.SemiBold,
            fontSize = (32 * scale).sp,
            lineHeight = (40 * scale).sp
        ),
        headlineMedium = TextStyle(
            fontFamily = typeface,
            fontWeight = FontWeight.SemiBold,
            fontSize = (28 * scale).sp,
            lineHeight = (36 * scale).sp
        ),
        headlineSmall = TextStyle(
            fontFamily = typeface,
            fontWeight = FontWeight.SemiBold,
            fontSize = (24 * scale).sp,
            lineHeight = (32 * scale).sp
        ),
        titleLarge = TextStyle(
            fontFamily = typeface,
            fontWeight = FontWeight.SemiBold,
            fontSize = (22 * scale).sp,
            lineHeight = (28 * scale).sp
        ),
        titleMedium = TextStyle(
            fontFamily = typeface,
            fontWeight = FontWeight.Medium,
            fontSize = (16 * scale).sp,
            lineHeight = (24 * scale).sp
        ),
        titleSmall = TextStyle(
            fontFamily = typeface,
            fontWeight = FontWeight.Medium,
            fontSize = (14 * scale).sp,
            lineHeight = (20 * scale).sp
        ),
        bodyLarge = TextStyle(
            fontFamily = typeface,
            fontWeight = FontWeight.Normal,
            fontSize = (16 * scale).sp,
            lineHeight = (24 * scale).sp
        ),
        bodyMedium = TextStyle(
            fontFamily = typeface,
            fontWeight = FontWeight.Normal,
            fontSize = (14 * scale).sp,
            lineHeight = (20 * scale).sp
        ),
        bodySmall = TextStyle(
            fontFamily = typeface,
            fontWeight = FontWeight.Normal,
            fontSize = (12 * scale).sp,
            lineHeight = (16 * scale).sp
        ),
        labelLarge = TextStyle(
            fontFamily = typeface,
            fontWeight = FontWeight.Medium,
            fontSize = (14 * scale).sp,
            lineHeight = (20 * scale).sp
        ),
        labelMedium = TextStyle(
            fontFamily = typeface,
            fontWeight = FontWeight.Medium,
            fontSize = (12 * scale).sp,
            lineHeight = (16 * scale).sp
        ),
        labelSmall = TextStyle(
            fontFamily = typeface,
            fontWeight = FontWeight.Medium,
            fontSize = (11 * scale).sp,
            lineHeight = (16 * scale).sp
        )
    )
}
