package com.metadent.foodhub_android.data.models


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("name")
    val name: String
)