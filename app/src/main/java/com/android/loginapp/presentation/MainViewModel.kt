package com.android.loginapp.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.loginapp.model.LoginRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(
    private val repository: LoginRepository
): ViewModel() {

    private val _isSignIn = MutableLiveData(false)
     val isSignIn = _isSignIn


    fun isSignIn() = viewModelScope.launch{
        try {
            _isSignIn.value = repository.isSignIn()
        }catch (e: Exception){
            _isSignIn.value = false
        }

    }

}