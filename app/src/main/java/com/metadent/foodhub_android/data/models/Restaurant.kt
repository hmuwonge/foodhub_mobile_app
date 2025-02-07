package com.metadent.foodhub_android.data.models


import com.google.gson.annotations.SerializedName

data class Restaurant(
    @SerializedName("address")
    val address: String,
    @SerializedName("categoryId")
    val categoryId: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("distance")
    val distance: Double,
    @SerializedName("id")
    val id: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("name")
    val name: String,
    @SerializedName("ownerId")
    val ownerId: String
)