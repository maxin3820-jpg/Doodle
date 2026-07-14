package com.doodle.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.doodle.app.data.settings.*

@Composable
fun DoodleTheme(
    themeMode: ThemeMode = ThemeMode.FollowSystem,
    accentColor: AccentColor = AccentColor.Blue,
    backgroundColor: BackgroundColor = BackgroundColor.White,
    fontFamily: FontFamily = FontFamily.Roboto,
    fontSize: FontSize = FontSize.Medium,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
        ThemeMode.FollowSystem -> isSystemInDarkTheme()
    }

    // Only rebuild ColorScheme when inputs actually change — not on every recomposition
    val colorScheme = remember(darkTheme, accentColor, backgroundColor) {
        buildColorScheme(darkTheme, accentColor, backgroundColor)
    }

    // Only rebuild Typography when font settings change
    val typography = remember(fontFamily, fontSize) {
        getTypography(fontFamily, fontSize)
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        val statusBarColor = colorScheme.background.toArgb()
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = statusBarColor
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}

private fun buildColorScheme(
    darkTheme: Boolean,
    accentColor: AccentColor,
    backgroundColor: BackgroundColor
): ColorScheme {
    val primary = when (accentColor) {
        AccentColor.Blue   -> if (darkTheme) Color(0xFF90CAF9) else Color(0xFF1976D2)
        AccentColor.Green  -> if (darkTheme) Color(0xFF81C784) else Color(0xFF388E3C)
        AccentColor.Purple -> if (darkTheme) Color(0xFFCE93D8) else Color(0xFF7B1FA2)
        AccentColor.Orange -> if (darkTheme) Color(0xFFFFB74D) else Color(0xFFF57C00)
        AccentColor.Red    -> if (darkTheme) Color(0xFFE57373) else Color(0xFFD32F2F)
        AccentColor.Pink   -> if (darkTheme) Color(0xFFF48FB1) else Color(0xFFC2185B)
    }

    val background = when (backgroundColor) {
        BackgroundColor.White     -> if (darkTheme) Color(0xFF121212) else Color(0xFFFFFFFF)
        BackgroundColor.Black     -> if (darkTheme) Color(0xFF000000) else Color(0xFFF5F5F5)
        BackgroundColor.LightGray -> if (darkTheme) Color(0xFF1E1E1E) else Color(0xFFF0F0F0)
        BackgroundColor.Cream     -> if (darkTheme) Color(0xFF2C2416) else Color(0xFFFFF8E1)
        BackgroundColor.SoftBlue  -> if (darkTheme) Color(0xFF1A1F2E) else Color(0xFFE3F2FD)
        BackgroundColor.SoftGreen -> if (darkTheme) Color(0xFF1B2E1F) else Color(0xFFE8F5E9)
    }

    return if (darkTheme) {
        darkColorScheme(
            primary = primary,
            onPrimary = Color.White,
            primaryContainer = primary.copy(alpha = 0.3f),
            onPrimaryContainer = primary,
            background = background,
            onBackground = Color(0xFFE0E0E0),
            surface = background,
            onSurface = Color(0xFFE0E0E0),
            surfaceVariant = Color(0xFF2C2C2C),
            onSurfaceVariant = Color(0xFFB0B0B0)
        )
    } else {
        lightColorScheme(
            primary = primary,
            onPrimary = Color.White,
            primaryContainer = primary.copy(alpha = 0.1f),
            onPrimaryContainer = primary,
            background = background,
            onBackground = Color(0xFF212121),
            surface = background,
            onSurface = Color(0xFF212121),
            surfaceVariant = Color(0xFFF5F5F5),
            onSurfaceVariant = Color(0xFF616161)
        )
    }
}
