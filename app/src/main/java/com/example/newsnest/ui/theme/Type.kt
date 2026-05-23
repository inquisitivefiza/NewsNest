package com.example.newsnest.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ─── Using only default system font families ──────────────────────────────────
// FontFamily.Serif     → device serif font (looks close to a display serif)
// FontFamily.SansSerif → device sans-serif font (clean, readable)

// Public alias so components can reference DmSans by name without R.font files
val DmSans       = FontFamily.SansSerif
val DmSerifDisplay = FontFamily.Serif

val NewsNestTypography = Typography(

    // Hero article title
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontSize   = 26.sp,
        lineHeight = 34.sp,
    ),

    // Article detail screen title
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontSize   = 22.sp,
        lineHeight = 30.sp,
    ),

    // ArticleDetailScreen — article.title
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontSize   = 20.sp,
        lineHeight = 28.sp,
    ),

    // ArticleCard — article.title in list
    titleMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontSize   = 15.sp,
        lineHeight = 22.sp,
    ),

    // Article description / body
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize   = 14.sp,
        lineHeight = 22.sp,
    ),

    bodySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize   = 13.sp,
        lineHeight = 20.sp,
    ),

    // Source tag, timestamps, category pills
    labelSmall = TextStyle(
        fontFamily    = FontFamily.SansSerif,
        fontWeight    = FontWeight.Medium,
        fontSize      = 11.sp,
        lineHeight    = 16.sp,
        letterSpacing = 0.06.sp,
    ),

    labelMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize   = 12.sp,
        lineHeight = 16.sp,
    ),
)