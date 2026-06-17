package com.dogdex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.dogdex.ui.navigation.DogDexNavHost
import com.dogdex.ui.theme.DogDexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val rootViewModel: RootViewModel = hiltViewModel()
            val settings by rootViewModel.settings.collectAsState()

            DogDexTheme(
                darkTheme = settings.darkMode,
                metricUnits = settings.metricUnits,
            ) {
                DogDexNavHost()
            }
        }
    }
}
