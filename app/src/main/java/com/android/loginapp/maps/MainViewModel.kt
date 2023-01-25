package com.android.loginapp.maps

import androidx.lifecycle.ViewModel
import com.android.loginapp.maps.model.PreferencesManager

class MainViewModel(private val preferencesManager: PreferencesManager) : ViewModel() {



    fun saveLocation(lat: Double,lon: Double){
        preferencesManager.saveLocation(lat,lon)
    }

}