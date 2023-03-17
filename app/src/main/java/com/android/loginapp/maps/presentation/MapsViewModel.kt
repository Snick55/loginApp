package com.android.loginapp.maps.presentation


import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.loginapp.login.model.LoginRepository
import com.android.loginapp.maps.model.PreferencesManager
import com.yandex.mapkit.traffic.TrafficLayer
import kotlinx.coroutines.launch

class MapsViewModel(
    private val preferencesManager: PreferencesManager,
    private val loginRepository: LoginRepository,
    private val currentNameCommunication: CurrentNameCommunication,
    private val locationCommunication: LocationCommunication
) : ViewModel(), SuccessRequestCallback {

    private  var isTrafficActive = false

    fun getUsername() = viewModelScope.launch {
        currentNameCommunication.map(loginRepository.getName())
    }

    fun signOut() = viewModelScope.launch {
        loginRepository.signOut()
    }

    fun changeName(name: String) = viewModelScope.launch {
        loginRepository.changeName(name, this@MapsViewModel)
    }

    override fun success() {
        getUsername()
    }

    fun getLocation() {
        val location = preferencesManager.getLocation()
        val lat = location.first.toDouble()
        val lon = location.second.toDouble()
        locationCommunication.map(Pair(lat, lon))
    }

    fun traffic(trafficLayer: TrafficLayer){
        trafficLayer.isTrafficVisible = !isTrafficActive
        isTrafficActive = !isTrafficActive
    }

    fun observeName(owner: LifecycleOwner, observer: Observer<String>){
        currentNameCommunication.observe(owner, observer)
    }

    fun observeLocation(owner: LifecycleOwner, observer: Observer<Pair<Double, Double>>){
        locationCommunication.observe(owner, observer)
    }

}