package com.android.loginapp.maps.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.loginapp.login.model.LoginRepository
import com.android.loginapp.maps.model.PreferencesManager
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MapsViewModel(
    private val preferencesManager: PreferencesManager,
    private val loginRepository: LoginRepository
): ViewModel() {

    private val _currentName = MutableLiveData<String>()
    val currentName = _currentName

     fun getUsername() = viewModelScope.launch{
      _currentName.value = loginRepository.getName()
    }

    fun signOut() = viewModelScope.launch {
        loginRepository.signOut()
    }

}