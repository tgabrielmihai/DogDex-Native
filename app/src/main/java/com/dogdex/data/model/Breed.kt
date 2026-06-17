package com.dogdex.data.model

/** Domain model used across the UI. Decoupled from both the API DTO and the Room entity. */
data class Breed(
    val id: Int,
    val name: String,
    val temperament: List<String>,
    val origin: String,
    val lifeSpan: String,
    val weightMetric: String,
    val weightImperial: String,
    val heightMetric: String,
    val heightImperial: String,
    val bredFor: String,
    val breedGroup: String,
    val imageUrl: String,
) {
    val hasImage: Boolean get() = imageUrl.isNotBlank()

    /** Weight string for the user's chosen unit system. */
    fun weight(metric: Boolean): String =
        if (metric) "$weightMetric kg" else "$weightImperial lbs"

    /** Height string for the user's chosen unit system. */
    fun height(metric: Boolean): String =
        if (metric) "$heightMetric cm" else "$heightImperial in"

    /** The Dog API has no free-text description, so we synthesize an "About"
     *  paragraph from the structured fields it does provide. */
    fun aboutText(): String {
        val parts = mutableListOf<String>()
        parts += "The $name is a ${breedGroup.ifBlank { "well-known" }} breed" +
            (if (origin.isNotBlank()) " originating from $origin." else ".")
        if (bredFor.isNotBlank()) parts += "Originally bred for ${bredFor.lowercase()}."
        if (temperament.isNotEmpty()) {
            parts += "Known for being ${temperament.joinToString(", ").lowercase()}."
        }
        if (lifeSpan.isNotBlank()) parts += "Typical life span is $lifeSpan."
        return parts.joinToString(" ")
    }
}
