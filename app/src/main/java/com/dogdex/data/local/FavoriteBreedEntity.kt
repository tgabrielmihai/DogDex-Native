package com.dogdex.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** A breed the user saved. Stored in full so the Favorites screen works fully offline. */
@Entity(tableName = "favorite_breeds")
data class FavoriteBreedEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val temperament: String,
    val origin: String,
    val lifeSpan: String,
    val weightMetric: String,
    val weightImperial: String,
    val heightMetric: String,
    val heightImperial: String,
    val bredFor: String,
    val breedGroup: String,
    val imageUrl: String,
)
