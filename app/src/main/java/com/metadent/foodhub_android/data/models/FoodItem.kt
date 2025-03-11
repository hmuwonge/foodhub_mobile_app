package com.metadent.foodhub_android.data.models

data class FoodItem(
    val arModelUrl: Any,
    val createdAt: String,
    val description: String,
    val id: String,
    val imageUrl: String,
    val name: String,
    val price: Double,
    val restaurantId: String
)