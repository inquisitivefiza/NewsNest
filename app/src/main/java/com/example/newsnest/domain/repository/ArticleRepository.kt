package com.example.domain.repository





import com.example.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    fun getArticles(): Flow<List<Article>>
    fun getArticles(category: String): Flow<List<Article>>
    suspend fun refreshArticles(category: String = "general")
}