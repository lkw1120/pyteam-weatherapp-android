package com.lkw1120.weatherapp.repository

import android.accounts.NetworkErrorException
import com.lkw1120.weatherapp.datasource.RemoteDataSource
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface NetworkRepository {

    suspend fun getWeatherInfo(lat: Double, lon: Double, unit: String): Flow<String>

    suspend fun getReverseGeocoding(lat: Double, lon: Double): Flow<String>

    suspend fun getGeocoding(location: String): Flow<String>
}

class NetworkRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : NetworkRepository {

    override suspend fun getWeatherInfo(lat: Double, lon: Double, unit: String) = flow {
        val response = remoteDataSource.getWeatherInfo(
            lat = lat,
            lon = lon,
            unit = unit
        )
        response.apply {
            suspendOnSuccess {
                emit(data)
            }
            suspendOnError {
                throw Exception(errorBody.toString())
            }
            suspendOnException {
                throw NetworkErrorException(exception.message)
            }
        }
    }

    override suspend fun getReverseGeocoding(lat: Double, lon: Double): Flow<String> = flow {
        val response = remoteDataSource.getReverseGeocoding(
            lat = lat,
            lon = lon,
        )
        response.apply {
            suspendOnSuccess {
                emit(data)
            }
            suspendOnError {
                throw Exception(errorBody.toString())
            }
            suspendOnException {
                throw NetworkErrorException(exception.message)
            }
        }
    }

    override suspend fun getGeocoding(location: String): Flow<String> = flow {
        val response = remoteDataSource.getGeocoding(
            location = location
        )
        response.apply {
            suspendOnSuccess {
                emit(data)
            }
            suspendOnError {
                throw Exception(errorBody.toString())
            }
            suspendOnException {
                throw NetworkErrorException(exception.message)
            }
        }
    }
}