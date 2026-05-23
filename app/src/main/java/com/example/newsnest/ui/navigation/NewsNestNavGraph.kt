package com.example.newsnest.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.domain.model.Article
import com.example.newsnest.ui.SplashScreen
import com.example.newsnest.ui.detail.ArticleDetailScreen
import com.example.newsnest.ui.list.ArticleListScreen

import com.google.gson.Gson

sealed class Screen(val route: String) {
    object Splash        : Screen("splash")
    object ArticleList   : Screen("article_list")
    object ArticleDetail : Screen("article_detail")
}

@Composable
fun NewsNestNavGraph() {
    val navController = rememberNavController()
    val gson = Gson()

    NavHost(
        navController    = navController,
        startDestination = Screen.Splash.route       // ← splash is now first
    ) {

        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashComplete = {
                    navController.navigate(Screen.ArticleList.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }  // remove splash from back stack
                    }
                }
            )
        }

        composable(Screen.ArticleList.route) {
            ArticleListScreen(
                onArticleClick = { article ->
                    val json = gson.toJson(article)
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("article", json)
                    navController.navigate(Screen.ArticleDetail.route)
                }
            )
        }

        composable(Screen.ArticleDetail.route) {
            val json = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<String>("article")
            val article = gson.fromJson(json, Article::class.java)

            article?.let {
                ArticleDetailScreen(
                    article = it,
                    onBack  = { navController.popBackStack() }
                )
            }
        }
    }
}