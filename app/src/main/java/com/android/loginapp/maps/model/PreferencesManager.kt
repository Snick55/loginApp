package com.android.loginapp.maps.model


import android.annotation.SuppressLint
import android.content.SharedPreferences

interface PreferencesManager {

    fun saveLocation(lat: Double, lon: Double)

    fun getLocation(): Pair<Float, Float>

    class Base(private val sharedPreferences: SharedPreferences) : PreferencesManager {


        @SuppressLint("CommitPrefEdits")
        override fun saveLocation(lat: Double, lon: Double) {
            sharedPreferences.edit().putFloat(LAT_KEY, lat.toFloat()).apply()
            sharedPreferences.edit().putFloat(LON_KEY, lon.toFloat()).apply()

        }

        override fun getLocation(): Pair<Float, Float> {
                val lat = sharedPreferences.getFloat(LAT_KEY, 55.751574F)
                val lon = sharedPreferences.getFloat(LON_KEY, 37.573856F)
                return Pair(lat, lon)
        }
    }

    companion object {
        private const val LAT_KEY = "LAT_KEY"
        private const val LON_KEY = "LON_KEY"
    }

}