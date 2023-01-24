package com.android.loginapp.maps.model


import android.content.SharedPreferences

interface PreferencesManager {

    fun saveLocation(key: String, data: Double)

    fun getLocation(keyLat: String, keyLon: String): Pair<Float, Float>

    class Base(private val sharedPreferences: SharedPreferences) : PreferencesManager {

        override fun saveLocation(key: String, data: Double) {
            sharedPreferences.edit().putFloat(key, data.toFloat())
        }

        override fun getLocation(keyLat: String, keyLon: String): Pair<Float, Float> {
            val lat = sharedPreferences.getFloat(keyLat, 0F)
            val lon = sharedPreferences.getFloat(keyLon, 0F)
            return Pair(lat, lon)
        }
    }

}