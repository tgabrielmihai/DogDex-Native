package com.dogdex.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BreedDto(
    val id: Int,
    val name: String,
    @SerialName("temperament") val temperament: String? = null,
    @SerialName("origin") val origin: String? = null,
    @SerialName("life_span") val lifeSpan: String? = null,
    @SerialName("bred_for") val bredFor: String? = null,
    @SerialName("breed_group") val breedGroup: String? = null,
    @SerialName("reference_image_id") val referenceImageId: String? = null,
    val weight: MeasureDto? = null,
    val height: MeasureDto? = null,
    val image: ImageDto? = null,
)

@Serializable
data class MeasureDto(
    val imperial: String? = null,
    val metric: String? = null,
)

@Serializable
data class ImageDto(
    val id: String? = null,
    val url: String? = null,
)