package com.dogdex.ui.breedlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dogdex.ui.UiState
import com.dogdex.ui.components.BreedCard
import com.dogdex.ui.components.DogDexHeader
import com.dogdex.ui.components.ErrorState
import com.dogdex.ui.components.LoadingState
import androidx.compose.foundation.background

@Composable
fun BreedListScreen(
    onBreedClick: (Int) -> Unit,
    viewModel: BreedListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        DogDexHeader(title = "DogDex", subtitle = "Explore dog breeds")

        when (val s = state) {
            is UiState.Loading -> LoadingState()
            is UiState.Error -> ErrorState(message = s.message, onRetry = viewModel::load)
            is UiState.Success -> LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(s.data, key = { it.id }) { breed ->
                    BreedCard(
                        breed = breed,
                        isFavorite = breed.id in favoriteIds,
                        onClick = { onBreedClick(breed.id) },
                        onFavoriteToggle = { viewModel.toggleFavorite(breed) },
                    )
                }
            }
        }
    }
}
