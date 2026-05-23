package com.example.domain.model



data class Article(
    val id: Int = 0,
    val title: String,
    val description: String?,
    val url: String,
    val imageUrl: String?,
    val publishedAt: String,
    val source: String,
    val category: String = "general"

)