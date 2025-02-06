package com.metadent.foodhub_android.data

import com.metadent.foodhub_android.data.models.SignUpRequest
import com.metadent.foodhub_android.data.models.AuthResponse
import com.metadent.foodhub_android.data.models.OAuthRequest
import com.metadent.foodhub_android.data.models.SignInRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FoodApi {

    @POST("/auth/signup")
    suspend fun signUp(@Body request: SignUpRequest): Response<AuthResponse>

    @POST("/auth/signin")
    suspend fun signIn(@Body request: SignInRequest): Response<AuthResponse>

    @POST("/auth/oauth")
    suspend fun oAuth(@Body request: OAuthRequest): Response<AuthResponse>
}