package com.dogdex.ui.breedlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dogdex.data.model.Breed
import com.dogdex.data.repo.BreedRepository
import com.dogdex.data.repo.FavoritesRepository
import com.dogdex.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedListViewModel @Inject constructor(
    private val breedRepository: BreedRepository,
    private val favoritesRepository: FavoritesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<List<Breed>>>(UiState.Loading)
    val state: StateFlow<UiState<List<Breed>>> = _state.asStateFlow()

    val favoriteIds: StateFlow<Set<Int>> = favoritesRepository.favoriteIds
        .map { it.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    init {
        load()
    }

    fun load() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            runCatching { breedRepository.getBreeds() }
                .onSuccess { _state.value = UiState.Success(it) }
                .onFailure { _state.value = UiState.Error(it.message ?: "Failed to load breeds") }
        }
    }

    fun toggleFavorite(breed: Breed) {
        viewModelScope.launch { favoritesRepository.toggle(breed) }
    }
}
