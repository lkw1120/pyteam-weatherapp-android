package com.lkw1120.weatherapp.datasource.local

import android.content.Context
import androidx.room.*
import com.lkw1120.weatherapp.datasource.local.dao.LocationDao
import com.lkw1120.weatherapp.datasource.local.dao.WeatherDao
import com.lkw1120.weatherapp.datasource.local.entity.Converters
import com.lkw1120.weatherapp.datasource.local.entity.LocationEntity
import com.lkw1120.weatherapp.datasource.local.entity.WeatherEntity

@Database(
    entities = [
        WeatherEntity::class,
        LocationEntity::class
    ],
    exportSchema = false,
    version = 3
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
    abstract fun locationDao(): LocationDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context
        ): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "weather-app-db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}