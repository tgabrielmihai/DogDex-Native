package com.dogdex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteBreedEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}
