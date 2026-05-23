package com.example.newsnest.data.repository

import com.example.newsnest.data.local.ArticleDao
import com.example.newsnest.data.local.ArticleEntity
import com.example.domain.model.Article
import com.example.domain.repository.ArticleRepository
import com.example.newsnest.data.remote.ArticleDto
import com.example.newsnest.data.remote.NewsApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val dao: ArticleDao,
    private val api: NewsApiService
) : ArticleRepository {

    override fun getArticles(): Flow<List<Article>> {
        return dao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getArticles(category: String): Flow<List<Article>> {
        return dao.getByCategory(category).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshArticles(category: String) {
        val response = api.getTopHeadlines(
            country  = "us",
            apiKey   = "4b6a79a20a5b4a06ad749e574a35c4b6",
            category = category
        )

        if (response.status != "ok") return

        val entities = response.articles
            .filter { it.title != null && it.url != null }
            .map { it.toEntity(category) }

        dao.deleteByCategory(category)
        dao.insertAll(entities)
    }
}

fun ArticleEntity.toDomain() = Article(
    id          = id,
    title       = title,
    description = description,
    url         = url,
    imageUrl    = imageUrl,
    publishedAt = publishedAt,
    source      = source,
    category    = category
)

fun ArticleDto.toEntity(category: String) = ArticleEntity(
    title       = title ?: "",
    description = description,
    url         = url ?: "",
    imageUrl    = urlToImage,
    publishedAt = publishedAt ?: "",
    source      = source?.name ?: "Unknown",
    category    = category
)