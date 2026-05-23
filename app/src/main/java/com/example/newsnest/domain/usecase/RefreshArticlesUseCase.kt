package com.example.domain.usecase


import com.example.domain.repository.ArticleRepository
import javax.inject.Inject

class RefreshArticlesUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    // category is now passed into repository so correct API endpoint is called
    suspend operator fun invoke(category: String = "general") =
        repository.refreshArticles(category)
}