package com.dogdex.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    /** Flow so the Favorites UI auto-updates whenever a row is added/removed. */
    @Query("SELECT * FROM favorite_breeds ORDER BY name ASC")
    fun observeFavorites(): Flow<List<FavoriteBreedEntity>>

    /** Stream of saved ids, used by the list screen to render filled/empty hearts. */
    @Query("SELECT id FROM favorite_breeds")
    fun observeFavoriteIds(): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(breed: FavoriteBreedEntity)

    @Query("DELETE FROM favorite_breeds WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_breeds WHERE id = :id)")
    suspend fun isFavorite(id: Int): Boolean
}
