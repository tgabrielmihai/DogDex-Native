package com.dogdex.ui.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dogdex.ui.components.BreedCard
import com.dogdex.ui.components.DogDexHeader
import com.dogdex.ui.components.EmptyState
import com.dogdex.ui.theme.DogOrange

@Composable
fun FavoritesScreen(
    onBreedClick: (Int) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel(),
) {
    val favorites by viewModel.favorites.collectAsState()

    Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
        DogDexHeader(title = "My Collection", subtitle = "Your favorite breeds")
        OfflineBanner()

        if (favorites.isEmpty()) {
            EmptyState(message = "No favorites yet.\nTap the heart on a breed to save it here.")
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(favorites, key = { it.id }) { breed ->
                    BreedCard(
                        breed = breed,
                        isFavorite = true,
                        onClick = { onBreedClick(breed.id) },
                        onFavoriteToggle = { viewModel.remove(breed) },
                    )
                }
            }
        }
    }
}

@Composable
private fun OfflineBanner() {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DogOrange.copy(alpha = 0.12f))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Filled.CloudDone, contentDescription = null, tint = DogOrange, modifier = Modifier.size(18.dp))
        Text(
            "  Offline Access Enabled",
            style = MaterialTheme.typography.labelSmall,
            color = DogOrange,
        )
    }
}
