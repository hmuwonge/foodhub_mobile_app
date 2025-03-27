package com.metadent.foodhub_android.ui.navigation

import com.metadent.foodhub_android.data.models.FoodItem
import kotlinx.serialization.Serializable


@Serializable
object Login

@Serializable
object SignUp

@Serializable
object Auth

@Serializable
object Home

@Serializable
data class RestaurantDetails(
    val restaurantId:String,
    val restaurantName:String,
    val restaurantImageUrl:String
)

@Serializable
data class FoodDetails(val foodItem: FoodItem)

@Serializable
object Cart