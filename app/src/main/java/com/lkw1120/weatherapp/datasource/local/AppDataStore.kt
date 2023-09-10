package com.lkw1120.weatherapp.datasource.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

object AppDataStore {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("weather_app")

    private lateinit var dataStore: DataStore<Preferences>

    private val FIRST_FLAG = booleanPreferencesKey("FIRST_LOAD")
    private val UNITS = stringPreferencesKey("UNITS")

    fun getDataStore(context: Context): AppDataStore {
        dataStore = context.dataStore
        return this@AppDataStore
    }

    suspend fun getFirstLoad(): Boolean {
        var currentValue = false
        dataStore.edit { preferences ->
            currentValue = preferences[FIRST_FLAG] ?: false
        }
        return currentValue
    }

    suspend fun setFirstLoad() {
        dataStore.edit { preferences ->
            preferences[FIRST_FLAG] = true
        }
    }

    suspend fun getSettings(): HashMap<String, String> {
        val settings = hashMapOf<String, String>()
        dataStore.edit { preferences ->
            if (preferences[UNITS] == null) {
                preferences[UNITS] = "metric"
            }
            settings["UNITS"] = preferences[UNITS].toString()
        }
        return settings
    }

    suspend fun updateUnits(units: String) {
        dataStore.edit { preferences ->
            preferences[UNITS] = units
        }
    }
    /*
        suspend fun setLocationInfo(name: String, lat: Double, lon: Double) {
            val newData =  hashMapOf(
                "name" to name,
                "lat" to lat,
                "lon" to lon
            )
            dataStore.edit { preferences ->
                val json = preferences[LOCATION_INFO]?:""
                val type: Type = object : TypeToken<Array<Map<String, Any?>?>?>() {}.type
                val data = (Gson().fromJson(json,type) as Array<Map<String,Any?>?>?)?.toList()?:listOf()
                val list = mutableListOf<Map<String,Any?>?>()
                list.addAll(data)
                if(!list.contains(newData)) {
                    list.add(newData)
                }
                preferences[LOCATION_INFO] = Gson().toJson(list)
            }
        }

        suspend fun getLocationInfo(): List<Map<String,Any?>> {
            val list = mutableListOf<Map<String,Any?>>(
                hashMapOf(
                    "name" to "Now",
                    "lat" to 0.0,
                    "lon" to 0.0
                )
            )
            dataStore.edit { preferences ->
                val json = preferences[LOCATION_INFO]?:""
                val type: Type = object : TypeToken<Array<Map<String, Any?>>>() {}.type
                val data = (Gson().fromJson(json,type) as Array<Map<String,Any?>?>?)?.toList()?:listOf()
                data.forEach { item ->
                    item?.let { list.add(it) }
                }
            }
            return list
        }
    */
}
