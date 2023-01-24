package com.android.loginapp.maps

import androidx.lifecycle.ViewModel
import com.android.loginapp.maps.model.PreferencesManager

class MainViewModel(private val preferencesManager: PreferencesManager) : ViewModel() {



    fun saveLocation(lat: Double,lon: Double){
        preferencesManager.saveLocation(LAT_KEY,lat)
        preferencesManager.saveLocation(LON_KEY,lon)

    }


    companion object{
        const val LAT_KEY = "LAT_KEY"
        const val LON_KEY = "LON_KEY"
    }
}