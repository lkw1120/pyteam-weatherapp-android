package com.lkw1120.weatherapp.datasource.local.entity

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun fromDouble(value: Double): String {
        return value.toString()
    }

    @TypeConverter
    fun stringToDouble(double: String): Double {
        return double.toDouble()
    }
}