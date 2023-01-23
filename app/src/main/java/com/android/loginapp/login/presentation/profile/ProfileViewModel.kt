package com.android.loginapp.login.presentation.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.loginapp.login.model.LoginRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: LoginRepository
): ViewModel(),SuccessRequestCallback {

    private val _currentName = MutableLiveData<String>()
    val currentName = _currentName

    fun currentUser()=  viewModelScope.launch{
        val name = repository.getName()
        _currentName.value = name
    }

    fun signOut() = viewModelScope.launch{
        repository.signOut()
    }

    fun changeName(name: String) = viewModelScope.launch {
        if (_currentName.value == name) return@launch
        repository.changeName(name,this@ProfileViewModel)
    }

    override fun success() {
        currentUser()
    }
}