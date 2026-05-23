package com.example.newsnest.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ─── Light Color Scheme ───────────────────────────────────────────────────────
private val LightColorScheme = lightColorScheme(
    primary          = Accent,
    onPrimary        = SurfaceLight,
    secondary        = Gold,
    onSecondary      = Ink,
    background       = BgLight,
    onBackground     = TextHead,
    surface          = SurfaceLight,
    onSurface        = TextHead,
    surfaceVariant   = PillBg,
    onSurfaceVariant = TextBody,
    outline          = DividerLight,
    error            = Accent,
)

// ─── Dark Color Scheme ────────────────────────────────────────────────────────
private val DarkColorScheme = darkColorScheme(
    primary          = AccentLight,
    onPrimary        = SurfaceDark,
    secondary        = Gold,
    onSecondary      = Ink,
    background       = BgDark,
    onBackground     = TextHeadDark,
    surface          = SurfaceDark,
    onSurface        = TextHeadDark,
    surfaceVariant   = PillBgDark,
    onSurfaceVariant = TextBodyDark,
    outline          = DividerDark,
    error            = AccentLight,
)

// ─── Theme Entry Point ────────────────────────────────────────────────────────
@Composable
fun NewsNestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = NewsNestTypography,
        content     = content,
    )
}