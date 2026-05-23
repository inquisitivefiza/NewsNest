package com.example.newsnest.ui.list




import com.example.domain.model.Article

sealed interface ArticleUiState {
    data object Loading : ArticleUiState
    data class Success(val articles: List<Article>) : ArticleUiState
    data class Error(val message: String) : ArticleUiState
    data class Empty(val query: String) : ArticleUiState   // no results for search term
}