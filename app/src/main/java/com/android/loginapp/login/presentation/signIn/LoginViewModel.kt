package com.android.loginapp.login.presentation.signIn

import androidx.lifecycle.*
import com.android.loginapp.login.model.LoginRepository
import com.android.loginapp.login.presentation.AuthCallback
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModel(
    private val repository: LoginRepository,
    private val loginCommunication: LoginSuccessCommunication,
    private val stateCommunication: LoginStateCommunication
) : ViewModel(),AuthCallback {

    fun signIn(email: String, password: String) = viewModelScope.launch {
        stateCommunication.showProgress()
        try {
            repository.signIn(email, password, this@LoginViewModel)
        }catch (e: Exception){
            stateCommunication.map(e)
        }

    }

    fun stateFalse() {
        loginCommunication.map(false)
    }

    fun observeSuccess(owner: LifecycleOwner,observer: Observer<Boolean>){
        loginCommunication.observe(owner, observer)
    }

    override fun map(e: Exception) {
        stateCommunication.map(e)
    }

    override fun success() {
        loginCommunication.map(true)
        stateCommunication.hideProgress()
    }

    fun observeState(owner: LifecycleOwner,observer: Observer<LoginStateCommunication.State>){
        stateCommunication.observe(owner, observer)
    }
}