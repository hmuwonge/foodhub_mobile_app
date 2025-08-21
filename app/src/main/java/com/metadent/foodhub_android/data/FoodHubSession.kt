package com.metadent.foodhub_android.data

import android.content.Context
import android.content.SharedPreferences

class FoodHubSession(val context: Context) {
    val sharedPref: SharedPreferences = context.getSharedPreferences("foodHub",Context.MODE_PRIVATE)
    init {

    }

    fun storeToken(token:String){
        sharedPref.edit().putString("token",token).apply()

    }

    fun getToken():String?{
        sharedPref.getString("token",null)?.let {
            return it
        }
        return null
    }
}