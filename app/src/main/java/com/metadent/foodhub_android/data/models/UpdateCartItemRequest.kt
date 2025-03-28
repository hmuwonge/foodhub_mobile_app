package com.metadent.foodhub_android.data.models

data class UpdateCartItemRequest(
    val foodItemId: String,
    val quantity: Int
)