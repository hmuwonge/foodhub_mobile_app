package com.metadent.foodhub_android.data.models


import com.google.gson.annotations.SerializedName

data class RestaurantResponse(
    @SerializedName("data")
    val `data`: List<Restaurant>
)