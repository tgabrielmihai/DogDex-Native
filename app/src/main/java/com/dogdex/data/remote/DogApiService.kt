package com.dogdex.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface DogApiService {
    @GET("v1/breeds")
    suspend fun getBreeds(): List<BreedDto>

    @GET("v1/images/search")
    suspend fun getBreedImages(
        @Query("breed_ids") breedId: Int,
        @Query("limit") limit: Int = 1
    ): List<ImageDto>
}