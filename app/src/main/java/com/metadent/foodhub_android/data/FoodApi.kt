package com.metadent.foodhub_android.data

import com.metadent.foodhub_android.data.models.SignUpRequest
import com.metadent.foodhub_android.data.models.AuthResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FoodApi {

    @POST("/auth/signup")
    suspend fun signUp(@Body request: SignUpRequest): AuthResponse
}