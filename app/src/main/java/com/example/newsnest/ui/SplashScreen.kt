package com.example.newsnest.ui


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsnest.ui.theme.Accent
import com.example.newsnest.ui.theme.DmSans
import com.example.newsnest.ui.theme.DmSerifDisplay
import com.example.newsnest.ui.theme.TextHead
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val SplashBg = Color(0xFFFFFFFF)
private val TaglineColor = Color(0xFF9A9A9A)

@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {

    // Animation states
    val logoAlpha      = remember { Animatable(0f) }
    val logoOffsetY    = remember { Animatable(24f) }
    val taglineAlpha   = remember { Animatable(0f) }
    val dotAlpha       = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Logo slides up + fades in
        launch {
            logoAlpha.animateTo(
                1f,
                animationSpec = tween(durationMillis = 600, easing = EaseOutExpo)
            )
        }
        launch {
            logoOffsetY.animateTo(
                0f,
                animationSpec = tween(durationMillis = 600, easing = EaseOutExpo)
            )
        }

        // Tagline fades in after logo settles
        delay(500)
        taglineAlpha.animateTo(
            1f,
            animationSpec = tween(durationMillis = 400)
        )

        // Loading dot pulses in
        delay(200)
        dotAlpha.animateTo(
            1f,
            animationSpec = tween(durationMillis = 300)
        )

        // Hold then navigate
        delay(800)
        onSplashComplete()
    }

    Box(
        modifier          = Modifier
            .fillMaxSize()
            .background(SplashBg),
        contentAlignment  = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // ── Logo ──────────────────────────────────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .alpha(logoAlpha.value)
                    .offset { IntOffset(0, logoOffsetY.value.dp.roundToPx()) }
            ) {
                Text(
                    text       = "News",
                    fontFamily = DmSerifDisplay,
                    fontSize   = 42.sp,
                    color      = TextHead,
                    letterSpacing = (-0.5).sp,
                )
                Text(
                    text       = "Nest",
                    fontFamily = DmSerifDisplay,
                    fontSize   = 42.sp,
                    color      = Accent,
                    fontStyle  = FontStyle.Italic,
                    letterSpacing = (-0.5).sp,
                )
            }

            Spacer(Modifier.height(10.dp))

            // ── Tagline ───────────────────────────────────────────────────────
            Text(
                text       = "Stay informed. Stay ahead.",
                fontFamily = DmSans,
                fontSize   = 13.sp,
                color      = TaglineColor,
                letterSpacing = 0.04.sp,
                modifier   = Modifier.alpha(taglineAlpha.value)
            )
        }

        // ── Loading dot at bottom ─────────────────────────────────────────────
        Box(
            modifier         = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-48).dp)
                .alpha(dotAlpha.value),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.material3.CircularProgressIndicator(
                modifier    = Modifier
                    .height(18.dp)
                    .align(Alignment.Center),
                color       = Accent,
                strokeWidth = 1.5.dp,
            )
        }
    }
}