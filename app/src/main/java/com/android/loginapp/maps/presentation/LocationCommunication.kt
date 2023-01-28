package com.android.loginapp.maps.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

interface LocationCommunication {

    fun map(location: Pair<Double,Double>)

    fun observe(owner: LifecycleOwner,observer: Observer<Pair<Double,Double>>)


    class Base: LocationCommunication{
        private val currentLocation = MutableLiveData<Pair<Double, Double>>()

        override fun map(location: Pair<Double, Double>) {
            currentLocation.value = location
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<Pair<Double, Double>>) {
            currentLocation.observe(owner, observer)
        }
    }
}