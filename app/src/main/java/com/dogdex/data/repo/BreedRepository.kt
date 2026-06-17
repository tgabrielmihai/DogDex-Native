package com.dogdex.data.repo

import com.dogdex.data.model.Breed
import com.dogdex.data.model.toDomain
import com.dogdex.data.remote.DogApiService
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

/** Fetches breeds from The Dog API and keeps an in-memory cache so the Splash
 *  preload is reused by every screen (list, detail, quiz) without re-fetching. */
@Singleton
class BreedRepository @Inject constructor(
    private val api: DogApiService,
) {
    private val mutex = Mutex()
    private var cache: List<Breed> = emptyList()

    suspend fun getBreeds(forceRefresh: Boolean = false): List<Breed> = mutex.withLock {
        if (cache.isEmpty() || forceRefresh) {
            cache = api.getBreeds().map { dto ->
                val breed = dto.toDomain()

                if (breed.imageUrl.isNotBlank()) {
                    breed
                } else {
                    val imageUrl = runCatching {
                        api.getBreedImages(breed.id).firstOrNull()?.url.orEmpty()
                    }.getOrDefault("")

                    breed.copy(imageUrl = imageUrl)
                }
            }
        }
        cache
    }

    suspend fun getBreedById(id: Int): Breed? =
        getBreeds().firstOrNull { it.id == id }

    /** Called by the Splash screen to warm the cache before the user reaches the list. */
    suspend fun preload() {
        runCatching { getBreeds(forceRefresh = false) }
    }
}
