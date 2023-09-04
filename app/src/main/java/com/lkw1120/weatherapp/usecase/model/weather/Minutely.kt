package com.lkw1120.weatherapp.usecase.model.weather


import com.google.gson.annotations.SerializedName

data class Minutely(
    @SerializedName("dt")
    val dt: Int? = null,
    @SerializedName("precipitation")
    val precipitation: Double? = null
)