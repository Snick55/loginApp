package com.android.loginapp.maps.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.loginapp.login.model.LoginRepository
import com.android.loginapp.maps.model.PreferencesManager
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MapsViewModel(
    private val preferencesManager: PreferencesManager,
    private val loginRepository: LoginRepository
): ViewModel(),SuccessRequestCallback {

    private val _currentName = MutableLiveData<String>()
    val currentName = _currentName


    private val _currentLocation = MutableLiveData<Pair<Double,Double>>()
    val currentLocation:LiveData<Pair<Double,Double>> = _currentLocation

     fun getUsername() = viewModelScope.launch{
      _currentName.value = loginRepository.getName()
    }

    fun signOut() = viewModelScope.launch {
        loginRepository.signOut()
    }

    fun changeName(name: String) = viewModelScope.launch{
        if (_currentName.value == name) return@launch
        loginRepository.changeName(name,this@MapsViewModel)
    }

    override fun success() {
        getUsername()
    }

    fun getLocation()  {

       val location = preferencesManager.getLocation()
        val lat = location.first.toDouble()
        val lon = location.second.toDouble()
        _currentLocation.value = Pair(lat,lon)
    }



}