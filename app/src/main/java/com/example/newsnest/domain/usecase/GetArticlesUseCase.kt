package com.example.domain.usecase



import com.example.domain.model.Article
import com.example.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject




class GetArticlesUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    // category is now passed into repository so DB filters correctly
    operator fun invoke(category: String = "general"): Flow<List<Article>> =
        repository.getArticles(category)
}