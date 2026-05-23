package com.example.newsnest.di

import com.example.newsnest.data.repository.ArticleRepositoryImpl
import com.example.domain.repository.ArticleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindArticleRepository(
        impl: ArticleRepositoryImpl
    ): ArticleRepository
}