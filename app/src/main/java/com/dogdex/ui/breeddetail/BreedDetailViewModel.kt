package com.dogdex.ui.breeddetail

import androidx.lifecycle.SavedStateHandle
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
class BreedDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val breedRepository: BreedRepository,
    private val favoritesRepository: FavoritesRepository,
) : ViewModel() {

    private val breedId: Int = savedStateHandle.get<Int>("breedId") ?: -1

    private val _state = MutableStateFlow<UiState<Breed>>(UiState.Loading)
    val state: StateFlow<UiState<Breed>> = _state.asStateFlow()

    val isFavorite: StateFlow<Boolean> = favoritesRepository.favoriteIds
        .map { breedId in it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    init {
        viewModelScope.launch {
            runCatching { breedRepository.getBreedById(breedId) }
                .onSuccess { breed ->
                    _state.value = breed?.let { UiState.Success(it) }
                        ?: UiState.Error("Breed not found")
                }
                .onFailure { _state.value = UiState.Error(it.message ?: "Failed to load breed") }
        }
    }

    fun toggleFavorite() {
        val breed = (state.value as? UiState.Success)?.data ?: return
        viewModelScope.launch { favoritesRepository.toggle(breed) }
    }
}
