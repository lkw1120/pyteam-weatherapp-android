package com.lkw1120.weatherapp.di

import com.lkw1120.weatherapp.datasource.location.LocationTracker
import com.lkw1120.weatherapp.repository.DatabaseRepository
import com.lkw1120.weatherapp.repository.NetworkRepository
import com.lkw1120.weatherapp.usecase.DatabaseUseCase
import com.lkw1120.weatherapp.usecase.DatabaseUseCaseImpl
import com.lkw1120.weatherapp.usecase.LocationUseCase
import com.lkw1120.weatherapp.usecase.LocationUseCaseImpl
import com.lkw1120.weatherapp.usecase.NetworkUseCase
import com.lkw1120.weatherapp.usecase.NetworkUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Singleton
    @Provides
    fun provideNetworkUseCase(
        networkRepository: NetworkRepository
    ): NetworkUseCase {
        return NetworkUseCaseImpl(networkRepository)
    }

    @Singleton
    @Provides
    fun provideDatabaseUseCase(
        databaseRepository: DatabaseRepository
    ): DatabaseUseCase {
        return DatabaseUseCaseImpl(databaseRepository)
    }

    @Singleton
    @Provides
    fun provideLocationUseCase(
        locationTracker: LocationTracker
    ): LocationUseCase {
        return LocationUseCaseImpl(locationTracker)
    }
}