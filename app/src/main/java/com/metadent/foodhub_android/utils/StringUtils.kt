package com.metadent.foodhub_android.utils

import java.text.NumberFormat
import java.util.Currency

fun formatCurrency(value:Double): String{
    val currencyFormater= NumberFormat.getCurrencyInstance()
    currencyFormater.currency = Currency.getInstance("USH")
    return currencyFormater.format(value)
}