package com.dogdex.data.model

import com.dogdex.data.local.FavoriteBreedEntity
import com.dogdex.data.remote.BreedDto

private const val IMAGE_CDN = "https://cdn2.thedogapi.com/images"

/** Build a usable image URL: prefer the embedded image object, otherwise derive
 *  it from the reference image id (The Dog API serves these directly off its CDN). */
private fun BreedDto.resolveImageUrl(): String =
    image?.url ?: referenceImageId?.let { "$IMAGE_CDN/$it.jpg" } ?: ""

fun BreedDto.toDomain(): Breed = Breed(
    id = id,
    name = name,
    temperament = temperament?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList(),
    origin = origin.orEmpty().trim(),
    lifeSpan = lifeSpan.orEmpty().trim(),
    weightMetric = weight?.metric.orEmpty().trim(),
    weightImperial = weight?.imperial.orEmpty().trim(),
    heightMetric = height?.metric.orEmpty().trim(),
    heightImperial = height?.imperial.orEmpty().trim(),
    bredFor = bredFor.orEmpty().trim(),
    breedGroup = breedGroup.orEmpty().trim(),
    imageUrl = resolveImageUrl(),
)

fun Breed.toEntity(): FavoriteBreedEntity = FavoriteBreedEntity(
    id = id,
    name = name,
    temperament = temperament.joinToString(","),
    origin = origin,
    lifeSpan = lifeSpan,
    weightMetric = weightMetric,
    weightImperial = weightImperial,
    heightMetric = heightMetric,
    heightImperial = heightImperial,
    bredFor = bredFor,
    breedGroup = breedGroup,
    imageUrl = imageUrl,
)

fun FavoriteBreedEntity.toDomain(): Breed = Breed(
    id = id,
    name = name,
    temperament = temperament.split(",").map { it.trim() }.filter { it.isNotEmpty() },
    origin = origin,
    lifeSpan = lifeSpan,
    weightMetric = weightMetric,
    weightImperial = weightImperial,
    heightMetric = heightMetric,
    heightImperial = heightImperial,
    bredFor = bredFor,
    breedGroup = breedGroup,
    imageUrl = imageUrl,
)
