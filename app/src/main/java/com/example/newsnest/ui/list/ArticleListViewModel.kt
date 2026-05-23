package com.example.newsnest.ui.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetArticlesUseCase
import com.example.domain.usecase.RefreshArticlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val getArticlesUseCase: GetArticlesUseCase,
    private val refreshArticlesUseCase: RefreshArticlesUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryMap = mapOf(
        "For you"  to "general",
        "World"    to "general",
        "Tech"     to "technology",
        "Business" to "business",
        "Science"  to "science",
        "Health"   to "health",
    )

    val selectedCategory: StateFlow<String> = savedStateHandle.getStateFlow(
        key          = "category",
        initialValue = "For you"
    )

    // Search query state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Whether search bar is visible
    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val uiState: StateFlow<ArticleUiState> = selectedCategory
        .flatMapLatest { chipLabel ->
            val apiCategory = categoryMap[chipLabel] ?: "general"
            // Combine articles with search query — filter client-side
            combine(
                getArticlesUseCase(apiCategory),
                _searchQuery.debounce(300)      // wait 300ms after user stops typing
            ) { articles, query ->
                val filtered = if (query.isBlank()) articles
                else articles.filter { article ->
                    article.title.contains(query, ignoreCase = true) ||
                            article.description?.contains(query, ignoreCase = true) == true ||
                            article.source.contains(query, ignoreCase = true)
                }
                if (filtered.isEmpty() && query.isBlank()) ArticleUiState.Loading
                else if (filtered.isEmpty()) ArticleUiState.Empty(query)
                else ArticleUiState.Success(filtered)
            }
                .catch { e ->
                    emit(ArticleUiState.Error(e.message ?: "Something went wrong"))
                }
        }
        .stateIn(
            scope        = viewModelScope,
            started      = SharingStarted.WhileSubscribed(5000),
            initialValue = ArticleUiState.Loading
        )

    init { refresh() }

    fun selectCategory(category: String) {
        savedStateHandle["category"] = category
        _searchQuery.value = ""         // clear search when switching category
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                val apiCategory = categoryMap[selectedCategory.value] ?: "general"
                refreshArticlesUseCase(apiCategory)
            } catch (_: Exception) {}
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun toggleSearch() {
        _isSearchActive.value = !_isSearchActive.value
        if (!_isSearchActive.value) _searchQuery.value = ""  // clear on close
    }
}