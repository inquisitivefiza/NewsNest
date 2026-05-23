package com.example.newsnest.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsnest.ui.theme.Accent
import com.example.newsnest.ui.theme.AccentBlue
import com.example.newsnest.ui.theme.DmSans
import com.example.newsnest.ui.theme.GlassBorder
import com.example.newsnest.ui.theme.PillBg
import com.example.newsnest.ui.theme.SavedPink
import com.example.newsnest.ui.theme.TextBody
import com.example.newsnest.ui.theme.TextDim

val newsCategories = listOf("For you", "World", "Tech", "Business", "Science", "Health")

// ─── Category Chip Row ────────────────────────────────────────────────────────
// Active chip uses Accent (red). Tapping calls onSelect → ViewModel filters news.
@Composable
fun CategoryChipRow(
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier              = modifier.fillMaxWidth(),
        contentPadding        = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(newsCategories) { _, cat ->
            val active = cat == selected
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (active) Accent else PillBg)
                    .border(
                        width = 0.5.dp,
                        color = if (active) Accent else GlassBorder,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable { onSelect(cat) }
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text       = cat,
                    fontFamily = DmSans,
                    fontSize   = 12.sp,
                    color      = if (active) Color.White else TextBody,
                )
            }
        }
    }
}

// ─── Source Tag ───────────────────────────────────────────────────────────────
@Composable
fun SourceTag(source: String, modifier: Modifier = Modifier) {
    Row(
        modifier          = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(5.dp)
                .background(AccentBlue, CircleShape)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text          = source.uppercase(),
            fontFamily    = DmSans,
            fontSize      = 11.sp,
            color         = AccentBlue,
            letterSpacing = 0.06.sp,
        )
    }
}

// ─── Category Pill ────────────────────────────────────────────────────────────
@Composable
fun CategoryPill(label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(PillBg)
            .border(0.5.dp, GlassBorder, RoundedCornerShape(10.dp))
            .padding(horizontal = 7.dp, vertical = 2.dp)
    ) {
        Text(
            text       = label,
            fontFamily = DmSans,
            fontSize   = 10.sp,
            color      = TextBody,
        )
    }
}

// ─── Article Action Row ───────────────────────────────────────────────────────
// Heart icon turns red (Accent) when isSaved = true
@Composable
fun ArticleActionRow(
    timeLabel: String,
    isSaved:   Boolean   = false,
    onSave:    () -> Unit = {},
    onShare:   () -> Unit = {},
    modifier:  Modifier  = Modifier
) {
    Row(
        modifier          = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text  = timeLabel,
            style = MaterialTheme.typography.labelSmall,
            color = TextDim,
        )
        Spacer(Modifier.weight(1f))
        IconButton(onClick = onSave, modifier = Modifier.size(32.dp)) {
            Icon(
                imageVector        = if (isSaved) Icons.Filled.Favorite
                else Icons.Filled.FavoriteBorder,
                contentDescription = if (isSaved) "Unsave" else "Save",
                tint               = if (isSaved) Accent else TextDim,
                modifier           = Modifier.size(18.dp)
            )
        }
        IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) {
            Icon(
                imageVector        = Icons.Filled.Send,
                contentDescription = "Share",
                tint               = TextDim,
                modifier           = Modifier.size(18.dp)
            )
        }
    }
}

// ─── Offline Strip ────────────────────────────────────────────────────────────
@Composable
fun OfflineStrip(
    articleCount: Int,
    lastSynced:   String,
    modifier:     Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF2F2F2))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector        = Icons.Filled.Clear,
            contentDescription = null,
            tint               = TextDim,
            modifier           = Modifier.size(14.dp)
        )
        Text(
            text       = "Offline · $articleCount downloaded · Synced $lastSynced",
            fontFamily = DmSans,
            fontSize   = 11.sp,
            color      = TextDim,
        )
    }
}