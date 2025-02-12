package com.metadent.foodhub_android.data

import com.metadent.foodhub_android.data.models.SignUpRequest
import com.metadent.foodhub_android.data.models.AuthResponse
import com.metadent.foodhub_android.data.models.CategoriesResponse
import com.metadent.foodhub_android.data.models.OAuthRequest
import com.metadent.foodhub_android.data.models.RestaurantResponse
import com.metadent.foodhub_android.data.models.SignInRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FoodApi {

    @GET("/categories")
    suspend fun getCategories(): Response<CategoriesResponse>

    @GET("/restaurants")
    suspend fun getRestaurants(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
    ): Response<RestaurantResponse>

    @POST("/auth/signup")
    suspend fun signUp(@Body request: SignUpRequest): Response<AuthResponse>

    @POST("/auth/login")
    suspend fun signIn(@Body request: SignInRequest): Response<AuthResponse>

    @POST("/auth/oauth")
    suspend fun oAuth(@Body request: OAuthRequest): Response<AuthResponse>
}