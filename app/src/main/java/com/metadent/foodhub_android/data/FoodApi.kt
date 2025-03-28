package com.metadent.foodhub_android.data

import com.metadent.foodhub_android.data.models.AddToCartRequest
import com.metadent.foodhub_android.data.models.AddToCartResponse
import com.metadent.foodhub_android.data.models.SignUpRequest
import com.metadent.foodhub_android.data.models.AuthResponse
import com.metadent.foodhub_android.data.models.CartResponse
import com.metadent.foodhub_android.data.models.CategoriesResponse
import com.metadent.foodhub_android.data.models.FoodItemResponse
import com.metadent.foodhub_android.data.models.GenericMsgResponse
import com.metadent.foodhub_android.data.models.OAuthRequest
import com.metadent.foodhub_android.data.models.RestaurantResponse
import com.metadent.foodhub_android.data.models.SignInRequest
import com.metadent.foodhub_android.data.models.UpdateCartItemRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
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

    @GET("/restaurants/{restaurantId}/menu")
    suspend fun getFoodItemsForRestaurant(@Path("restaurantId") restaurantId:String):
            Response<FoodItemResponse>

    @POST("/cart")
    suspend fun addToCart(@Body request: AddToCartRequest): Response<AddToCartResponse>

    @GET("/cart")
    suspend fun getCart(): Response<CartResponse>

    @PATCH("/cart")
    suspend fun updateCart(@Body request: UpdateCartItemRequest): Response<GenericMsgResponse>

    @DELETE("/cart/{cartItemId}")
    suspend fun deleteCartItem(@Path("cartItemId") cartItemId: String): Response<GenericMsgResponse>

}