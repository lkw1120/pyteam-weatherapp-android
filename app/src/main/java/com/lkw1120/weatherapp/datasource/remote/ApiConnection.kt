package com.lkw1120.weatherapp.datasource.remote

import com.lkw1120.weatherapp.common.NetworkService.BASE_URL
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object ApiConnection {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(loggingInterceptor)
        connectTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
    }.build()

    private val client = Retrofit
        .Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    fun getService(): ApiService {
        return client.create(ApiService::class.java)
    }
}