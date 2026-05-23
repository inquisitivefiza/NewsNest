package com.example.newsnest.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.domain.model.Article
import com.example.newsnest.ui.components.ArticleActionRow
import com.example.newsnest.ui.components.SourceTag
import com.example.newsnest.ui.theme.Accent
import com.example.newsnest.ui.theme.AccentBlue
import com.example.newsnest.ui.theme.DmSans
import com.example.newsnest.ui.theme.TextBody
import com.example.newsnest.ui.theme.TextDim
import com.example.newsnest.ui.theme.TextHead

// ─── Local color constants (no Color.Blue anywhere) ───────────────────────────
private val ScreenBg  = Color(0xFFFFFFFF)   // pure white
private val CardBg    = Color(0xFFF5F5F5)   // light grey cards

// ─── Date formatter ───────────────────────────────────────────────────────────
fun formatDate(raw: String): String {
    return try {
        val parser    = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.US)
        val formatter = java.text.SimpleDateFormat("MMM dd, yyyy · h:mm a", java.util.Locale.US)
        formatter.format(parser.parse(raw)!!)
    } catch (e: Exception) {
        raw
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(
    article: Article,
    onBack:  () -> Unit
) {
    var isSaved by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = ScreenBg,          // ✅ white — NOT Color.Blue
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text          = article.source.uppercase(),
                        fontFamily    = DmSans,
                        fontSize      = 12.sp,
                        color         = AccentBlue,
                        letterSpacing = 0.08.sp,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint               = TextBody,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector        = Icons.Outlined.Search,
                            contentDescription = "Open in browser",
                            tint               = TextBody,
                            modifier           = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ScreenBg,  // ✅ white — NOT Transparent
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ScreenBg)           // ✅ white — NOT Blue
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Hero image ────────────────────────────────────────────────────
            article.imageUrl?.let { url ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                ) {
                    AsyncImage(
                        model              = url,
                        contentDescription = article.title,
                        modifier           = Modifier.fillMaxSize(),
                        contentScale       = ContentScale.Crop
                    )
                    // Subtle dark fade at bottom — no blue involved
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0x26000000),  // black at ~15% opacity
                                    ),
                                    startY = 120f,
                                )
                            )
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(Modifier.height(if (article.imageUrl != null) 16.dp else 12.dp))

                // ── Source + timestamp ────────────────────────────────────────
                Row {
                    SourceTag(source = article.source)
                    Text(
                        text       = "  ·  ${formatDate(article.publishedAt)}",
                        fontFamily = DmSans,
                        fontSize   = 11.sp,
                        color      = TextDim,
                    )
                }

                Spacer(Modifier.height(14.dp))

                // ── Title ─────────────────────────────────────────────────────
                Text(
                    text  = article.title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = TextHead,
                )

                Spacer(Modifier.height(16.dp))

                // ── Red accent divider ────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(2.dp)
                        .background(Accent, RoundedCornerShape(1.dp))
                )

                Spacer(Modifier.height(16.dp))

                // ── Description / body ────────────────────────────────────────
                article.description?.let { desc ->
                    Text(
                        text       = desc,
                        style      = MaterialTheme.typography.bodyMedium,
                        color      = TextBody,
                        lineHeight = 26.sp,
                    )
                    Spacer(Modifier.height(20.dp))
                }

                // ── Pull-quote card ───────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(CardBg)     // ✅ light grey — not white-on-white
                        .padding(16.dp)
                ) {
                    Text(
                        text       = "Continue reading the full article in your browser for more details.",
                        fontFamily = DmSans,
                        fontStyle  = FontStyle.Italic,
                        fontSize   = 13.sp,
                        color      = TextDim,
                        lineHeight = 20.sp,
                    )
                }

                Spacer(Modifier.height(20.dp))

                // ── Action row — heart turns red on tap ───────────────────────
                ArticleActionRow(
                    timeLabel = formatDate(article.publishedAt),
                    isSaved   = isSaved,
                    onSave    = { isSaved = !isSaved },
                    modifier  = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(CardBg)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}