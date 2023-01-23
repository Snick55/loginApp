package com.android.loginapp.login.presentation.signIn

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

interface LoginSuccessCommunication {

    fun map(result: Boolean)

    fun observe(owner: LifecycleOwner,observer: Observer<Boolean>)

    class Base: LoginSuccessCommunication{

        private val liveData = MutableLiveData(false)

        override fun map(result: Boolean) {
            liveData.value = result
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<Boolean>) {
            liveData.observe(owner, observer)
        }
    }

}