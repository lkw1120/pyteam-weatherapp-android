package com.lkw1120.weatherapp.usecase.model.geo


import com.google.gson.annotations.SerializedName

data class LocalNames(
    @SerializedName("ascii")
    val ascii: String? = null,
    @SerializedName("ca")
    val ca: String? = null,
    @SerializedName("de")
    val de: String? = null,
    @SerializedName("el")
    val el: String? = null,
    @SerializedName("en")
    val en: String? = null,
    @SerializedName("es")
    val es: String? = null,
    @SerializedName("et")
    val et: String? = null,
    @SerializedName("feature_name")
    val featureName: String? = null,
    @SerializedName("fr")
    val fr: String? = null,
    @SerializedName("ja")
    val ja: String? = null,
    @SerializedName("ko")
    val ko: String? = null,
    @SerializedName("zh")
    val zh: String? = null
)