package com.dogdex.data.repo

import com.dogdex.data.local.FavoriteDao
import com.dogdex.data.model.Breed
import com.dogdex.data.model.toDomain
import com.dogdex.data.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepository @Inject constructor(
    private val dao: FavoriteDao,
) {
    val favorites: Flow<List<Breed>> =
        dao.observeFavorites().map { list -> list.map { it.toDomain() } }

    val favoriteIds: Flow<List<Int>> = dao.observeFavoriteIds()

    suspend fun toggle(breed: Breed) {
        if (dao.isFavorite(breed.id)) dao.deleteById(breed.id)
        else dao.insert(breed.toEntity())
    }

    suspend fun remove(id: Int) = dao.deleteById(id)
}
