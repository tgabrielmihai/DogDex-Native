package com.dogdex.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Extension
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

object Routes {
    const val SPLASH = "splash"
    const val LIST = "list"
    const val FAVORITES = "favorites"
    const val QUIZ = "quiz"
    const val SETTINGS = "settings"
    const val DETAIL = "detail/{breedId}"
    fun detail(breedId: Int) = "detail/$breedId"
}

/** The four destinations shown in the bottom navigation bar. */
enum class BottomTab(val route: String, val label: String, val icon: ImageVector) {
    HOME(Routes.LIST, "Home", Icons.Outlined.Home),
    COLLECTION(Routes.FAVORITES, "Collection", Icons.Outlined.Bookmarks),
    TRIVIA(Routes.QUIZ, "Trivia", Icons.Outlined.Extension),
    SETTINGS(Routes.SETTINGS, "Settings", Icons.Outlined.Settings),
}
