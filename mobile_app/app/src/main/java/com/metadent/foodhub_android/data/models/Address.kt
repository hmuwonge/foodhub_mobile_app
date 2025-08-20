package com.metadent.foodhub_android.data.models

data class Address(
    val id:String?=null,
    val userId: String?=null,
    val addressLine1: String?=null,
    val addressLine2: String?=null,
    val city: String?=null,
    val state: String?=null,
    val zipcode: String?=null,
    val country: String?=null,
    val latitude: Double?=null,
    val longitude: Double?=null,
)