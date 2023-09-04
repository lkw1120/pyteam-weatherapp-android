package com.lkw1120.weatherapp.di

import android.content.Context
import com.lkw1120.weatherapp.datasource.LocalDataSource
import com.lkw1120.weatherapp.datasource.LocalDataSourceImpl
import com.lkw1120.weatherapp.datasource.RemoteDataSource
import com.lkw1120.weatherapp.datasource.RemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideLocalDataSource(
        @ApplicationContext appContext: Context
    ): LocalDataSource {
        return LocalDataSourceImpl(appContext)
    }

    @Singleton
    @Provides
    fun provideRemoteDataSource(
        @ApplicationContext appContext: Context
    ): RemoteDataSource {
        return RemoteDataSourceImpl(appContext)
    }

}