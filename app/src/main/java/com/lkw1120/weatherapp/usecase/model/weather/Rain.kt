package com.lkw1120.weatherapp.usecase.model.weather


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h")
    val h: Double? = null
)