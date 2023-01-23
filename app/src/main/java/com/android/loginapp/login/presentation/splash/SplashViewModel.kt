package com.android.loginapp.login.presentation.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.loginapp.login.model.LoginRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

class SplashViewModel(
    private val repository: LoginRepository
) : ViewModel() {


    private val _isSignIn: MutableLiveData<Boolean> = MutableLiveData()
    val isSignIn = _isSignIn


    fun isSignIn() = viewModelScope.launch {
        try {
            delay(2000)
            _isSignIn.value = repository.isSignIn()
        } catch (e: Exception) {
            _isSignIn.value = false
        }

    }

}