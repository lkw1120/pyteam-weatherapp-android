package com.lkw1120.weatherapp.di

import android.content.Context
import com.lkw1120.weatherapp.datasource.LocalDataSource
import com.lkw1120.weatherapp.datasource.LocalDataSourceImpl
import com.lkw1120.weatherapp.datasource.RemoteDataSource
import com.lkw1120.weatherapp.datasource.RemoteDataSourceImpl
import com.lkw1120.weatherapp.datasource.local.AppDataStore
import com.lkw1120.weatherapp.datasource.local.AppDatabase
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
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = AppDatabase.getDatabase(context)

    @Singleton
    @Provides
    fun provideAppDataStore(
        @ApplicationContext context: Context
    ): AppDataStore = AppDataStore.getDataStore(context)

    @Singleton
    @Provides
    fun provideLocalDataSource(
        appDatabase: AppDatabase,
        appDatastore: AppDataStore,
    ): LocalDataSource {
        return LocalDataSourceImpl(
            appDatabase = appDatabase,
            appDataStore = appDatastore
        )
    }

    @Singleton
    @Provides
    fun provideRemoteDataSource(
        @ApplicationContext appContext: Context
    ): RemoteDataSource {
        return RemoteDataSourceImpl(appContext)
    }

}