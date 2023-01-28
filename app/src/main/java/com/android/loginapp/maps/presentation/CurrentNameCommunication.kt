package com.android.loginapp.maps.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

interface CurrentNameCommunication {

    fun map(name: String)

    fun observe(owner: LifecycleOwner,observer: Observer<String>)


    class Base: CurrentNameCommunication{
        private val livedata = MutableLiveData<String>()

        override fun map(name: String) {
            livedata.value = name
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<String>) {
            livedata.observe(owner, observer)
        }
    }
}