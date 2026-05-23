package com.example.newsnest.ui.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.domain.model.Article
import com.example.newsnest.ui.components.CategoryChipRow
import com.example.newsnest.ui.components.CategoryPill
import com.example.newsnest.ui.components.OfflineStrip
import com.example.newsnest.ui.components.SourceTag
import com.example.newsnest.ui.theme.Accent
import com.example.newsnest.ui.theme.AccentBlue
import com.example.newsnest.ui.theme.DmSans
import com.example.newsnest.ui.theme.DmSerifDisplay
import com.example.newsnest.ui.theme.GlassBorder
import com.example.newsnest.ui.theme.TextBody
import com.example.newsnest.ui.theme.TextDim
import com.example.newsnest.ui.theme.TextHead

private val ScreenBg    = Color(0xFFFFFFFF)
private val CardBg      = Color(0xFFFAFAFA)
private val ThumbnailBg = Color(0xFFEEEEEE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleListScreen(
    onArticleClick: (Article) -> Unit,
    viewModel: ArticleListViewModel = hiltViewModel()
) {
    val uiState          by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val searchQuery      by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isSearchActive   by viewModel.isSearchActive.collectAsStateWithLifecycle()

    val savedArticles    = remember { mutableStateMapOf<String, Boolean>() }
    val focusRequester   = remember { FocusRequester() }
    val keyboard         = LocalSoftwareKeyboardController.current

    // Auto-focus keyboard when search opens
    LaunchedEffect(isSearchActive) {
        if (isSearchActive) focusRequester.requestFocus()
    }

    Scaffold(
        containerColor = ScreenBg,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ScreenBg)
            ) {
                TopAppBar(
                    title = {
                        // Animate between logo and search bar
                        AnimatedVisibility(
                            visible = !isSearchActive,
                            enter   = fadeIn(),
                            exit    = fadeOut()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text       = "News",
                                    fontFamily = DmSerifDisplay,
                                    fontSize   = 24.sp,
                                    color      = TextHead,
                                )
                                Text(
                                    text       = "Nest",
                                    fontFamily = DmSerifDisplay,
                                    fontSize   = 24.sp,
                                    color      = Accent,
                                    fontStyle  = FontStyle.Italic,
                                )
                            }
                        }
                        AnimatedVisibility(
                            visible = isSearchActive,
                            enter   = fadeIn() + slideInVertically(),
                            exit    = fadeOut() + slideOutVertically()
                        ) {
                            TextField(
                                value         = searchQuery,
                                onValueChange = viewModel::onSearchQueryChange,
                                placeholder   = {
                                    Text(
                                        "Search articles…",
                                        fontFamily = DmSans,
                                        fontSize   = 14.sp,
                                        color      = TextDim
                                    )
                                },
                                singleLine    = true,
                                modifier      = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester),
                                colors        = TextFieldDefaults.colors(
                                    focusedContainerColor   = ScreenBg,
                                    unfocusedContainerColor = ScreenBg,
                                    focusedIndicatorColor   = Accent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    cursorColor             = Accent,
                                    focusedTextColor        = Color.Black,
                                    unfocusedTextColor      = Color.Black,
                                ),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(
                                    onSearch = { keyboard?.hide() }
                                )
                            )
                        }
                    },
                    actions = {
                        // Toggle between search icon and close icon
                        IconButton(onClick = {
                            viewModel.toggleSearch()
                            if (isSearchActive) keyboard?.hide()
                        }) {
                            Icon(
                                imageVector = if (isSearchActive) Icons.Outlined.Close
                                else Icons.Outlined.Search,
                                contentDescription = if (isSearchActive) "Close search"
                                else "Search",
                                tint     = TextBody,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        if (!isSearchActive) {
                            IconButton(onClick = {}) {
                                Icon(
                                    Icons.Outlined.Notifications, "Notifications",
                                    tint     = TextBody,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            IconButton(onClick = {}) {
                                Icon(
                                    Icons.Outlined.Person, "Profile",
                                    tint     = TextBody,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = ScreenBg)
                )

                if (!isSearchActive) {
                    CategoryChipRow(
                        selected = selectedCategory,
                        onSelect = { viewModel.selectCategory(it) },
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ScreenBg)
                .padding(paddingValues)
        ) {
            when (val state = uiState) {

                is ArticleUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier    = Modifier.align(Alignment.Center),
                        color       = Accent,
                        strokeWidth = 2.dp
                    )
                }

                is ArticleUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text       = state.message,
                            color      = MaterialTheme.colorScheme.error,
                            fontFamily = DmSans,
                            fontSize   = 14.sp,
                        )
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = { viewModel.refresh() },
                            colors  = ButtonDefaults.buttonColors(containerColor = Accent),
                            shape   = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Outlined.Refresh, null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Retry", fontFamily = DmSans)
                        }
                    }
                }

                // No results for search query
                is ArticleUiState.Empty -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Outlined.Search, null,
                            tint     = TextDim,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text       = "No results for \"${state.query}\"",
                            fontFamily = DmSans,
                            fontSize   = 14.sp,
                            color      = TextDim,
                        )
                    }
                }

                is ArticleUiState.Success -> {
                    val articles = state.articles
                    LazyColumn(
                        contentPadding      = PaddingValues(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        if (articles.isNotEmpty()) {
                            item {
                                val key   = articles.first().url ?: articles.first().title
                                val saved = savedArticles[key] == true
                                HeroArticleCard(
                                    article  = articles.first(),
                                    isSaved  = saved,
                                    onSave   = { savedArticles[key] = !saved },
                                    onClick  = { onArticleClick(articles.first()) },
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                                )
                            }
                        }

                        item {
                            Text(
                                text          = if (searchQuery.isBlank()) "Latest stories"
                                else "Results for \"$searchQuery\"",
                                fontFamily    = DmSans,
                                fontSize      = 10.sp,
                                color         = TextDim,
                                letterSpacing = 0.1.sp,
                                modifier      = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
                            )
                        }

                        itemsIndexed(articles.drop(1)) { _, article ->
                            val key   = article.url ?: article.title
                            val saved = savedArticles[key] == true
                            ArticleCard(
                                article = article,
                                isSaved = saved,
                                onSave  = { savedArticles[key] = !saved },
                                onClick = { onArticleClick(article) }
                            )
                        }

                        item {
                            OfflineStrip(
                                articleCount = articles.size,
                                lastSynced   = "2 hrs ago",
                                modifier     = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─── Hero Card ────────────────────────────────────────────────────────────────
@Composable
fun HeroArticleCard(
    article:  Article,
    isSaved:  Boolean,
    onSave:   () -> Unit,
    onClick:  () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(CardBg)
            .border(0.5.dp, GlassBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
    ) {
        Box(
            modifier         = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(ThumbnailBg),
            contentAlignment = Alignment.TopStart
        ) {
            article.imageUrl?.let { url ->
                AsyncImage(
                    model              = url,
                    contentDescription = article.title,
                    modifier           = Modifier.fillMaxSize(),
                    contentScale       = ContentScale.Crop
                )
            }
            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Accent)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text("Top story", fontFamily = DmSans, fontSize = 10.sp, color = Color.White)
            }
        }

        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment      = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SourceTag(article.source)
                Text("· ${article.publishedAt}", fontFamily = DmSans, fontSize = 11.sp, color = TextDim)
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text     = article.title,
                style    = MaterialTheme.typography.headlineMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color    = TextHead,
            )
            article.description?.let {
                Spacer(Modifier.height(6.dp))
                Text(text = it, style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis, color = TextBody)
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier              = Modifier.fillMaxWidth(),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("6 min read", style = MaterialTheme.typography.labelSmall, color = TextDim)
                IconButton(onClick = onSave, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector        = if (isSaved) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = if (isSaved) "Unsave" else "Save",
                        tint               = if (isSaved) Accent else TextDim,
                        modifier           = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

// ─── List Card ────────────────────────────────────────────────────────────────
@Composable
fun ArticleCard(
    article: Article,
    isSaved: Boolean,
    onSave:  () -> Unit,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(ScreenBg)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment     = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment      = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(article.source.uppercase(), fontFamily = DmSans, fontSize = 10.sp, color = AccentBlue, letterSpacing = 0.06.sp)
                CategoryPill(label = "World")
            }
            Spacer(Modifier.height(5.dp))
            Text(text = article.title, style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis, color = TextHead)
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(article.publishedAt, style = MaterialTheme.typography.labelSmall, color = TextDim)
                IconButton(onClick = onSave, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector        = if (isSaved) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = if (isSaved) "Unsave" else "Save",
                        tint               = if (isSaved) Accent else TextDim,
                        modifier           = Modifier.size(16.dp)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .size(width = 72.dp, height = 64.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(ThumbnailBg)
                .border(0.5.dp, GlassBorder, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            article.imageUrl?.let { url ->
                AsyncImage(model = url, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            }
        }
    }
    Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(GlassBorder))
}