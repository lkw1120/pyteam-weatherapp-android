package com.lkw1120.weatherapp.di

import com.lkw1120.weatherapp.datasource.LocalDataSource
import com.lkw1120.weatherapp.datasource.RemoteDataSource
import com.lkw1120.weatherapp.repository.DatabaseRepository
import com.lkw1120.weatherapp.repository.DatabaseRepositoryImpl
import com.lkw1120.weatherapp.repository.NetworkRepository
import com.lkw1120.weatherapp.repository.NetworkRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideNetworkRepository(
        remoteDataSource: RemoteDataSource
    ): NetworkRepository {
        return NetworkRepositoryImpl(remoteDataSource)
    }

    @Singleton
    @Provides
    fun provideDatabaseRepository(
        localDataSource: LocalDataSource
    ): DatabaseRepository {
        return DatabaseRepositoryImpl(localDataSource)
    }

}