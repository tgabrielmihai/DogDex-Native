package com.dogdex.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import com.dogdex.ui.theme.DogOrange

@Composable
fun DogDexBottomBar(
    currentRoute: String?,
    onTabSelected: (BottomTab) -> Unit,
) {
    NavigationBar {
        BottomTab.entries.forEach { tab ->
            NavigationBarItem(
                selected = currentRoute == tab.route,
                onClick = { onTabSelected(tab) },
                icon = { Icon(tab.icon, contentDescription = tab.label) },
                label = { Text(tab.label, textAlign = TextAlign.Center) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = DogOrange,
                    selectedTextColor = DogOrange,
                    indicatorColor = DogOrange.copy(alpha = 0.12f),
                ),
            )
        }
    }
}
