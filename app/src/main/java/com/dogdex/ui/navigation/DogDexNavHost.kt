package com.dogdex.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dogdex.ui.breeddetail.BreedDetailScreen
import com.dogdex.ui.breedlist.BreedListScreen
import com.dogdex.ui.favorites.FavoritesScreen
import com.dogdex.ui.quiz.QuizScreen
import com.dogdex.ui.settings.SettingsScreen
import com.dogdex.ui.splash.SplashScreen

@Composable
fun DogDexNavHost() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val showBottomBar = currentRoute in BottomTab.entries.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                DogDexBottomBar(
                    currentRoute = currentRoute,
                    onTabSelected = { tab ->
                        navController.navigate(tab.route) {
                            popUpTo(Routes.LIST) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.SPLASH,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Routes.SPLASH) {
                SplashScreen(
                    onReady = {
                        navController.navigate(Routes.LIST) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                        }
                    },
                )
            }
            composable(Routes.LIST) {
                BreedListScreen(onBreedClick = { id -> navController.navigate(Routes.detail(id)) })
            }
            composable(Routes.FAVORITES) {
                FavoritesScreen(onBreedClick = { id -> navController.navigate(Routes.detail(id)) })
            }
            composable(Routes.QUIZ) {
                QuizScreen(
                    onNavigateHome = {
                        navController.navigate(Routes.LIST) {
                            popUpTo(Routes.LIST) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
            composable(Routes.SETTINGS) {
                SettingsScreen()
            }
            composable(
                route = Routes.DETAIL,
                arguments = listOf(navArgument("breedId") { type = NavType.IntType }),
            ) { entry ->
                val breedId = entry.arguments?.getInt("breedId") ?: return@composable
                BreedDetailScreen(
                    breedId = breedId,
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}
