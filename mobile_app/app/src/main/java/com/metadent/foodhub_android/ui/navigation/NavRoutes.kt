package com.metadent.foodhub_android.ui.navigation

import com.metadent.foodhub_android.data.models.FoodItem
import kotlinx.serialization.Serializable

interface NavRoutes

@Serializable
object Login: NavRoutes

@Serializable
object SignUp:NavRoutes

@Serializable
object Auth:NavRoutes

@Serializable
object Home:NavRoutes

@Serializable
data class RestaurantDetails(
    val restaurantId:String,
    val restaurantName:String,
    val restaurantImageUrl:String
):NavRoutes

@Serializable
data class FoodDetails(val foodItem: FoodItem):NavRoutes

@Serializable
object Cart:NavRoutes

@Serializable
object Notification:NavRoutes

@Serializable
object AddressList:NavRoutes