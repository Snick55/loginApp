package com.android.loginapp.login.presentation.signUp

import androidx.lifecycle.*
import com.android.loginapp.login.model.*
import com.android.loginapp.login.presentation.AuthCallback
import kotlinx.coroutines.launch
import java.lang.Exception

class SignUpViewModel(
    private val repository: LoginRepository,
    private val successCommunication: SignUpSuccessCommunication,
    private val stateCommunication: SignUpStateCommunication
) : ViewModel(),AuthCallback {

    fun signUp(name:String,email: String, password: String, repeatPass: String) =
        viewModelScope.launch {
           stateCommunication.showProgress()
            try {
                repository.signUp(name,email, password, repeatPass, this@SignUpViewModel)
            } catch (e: Exception ) {
                stateCommunication.map(e)
            }
        }

    fun observeSuccess(owner: LifecycleOwner,observer: Observer<Boolean>){
        successCommunication.observe(owner, observer)
    }

    override fun map(e: Exception) {
        stateCommunication.map(e)
    }

    override fun success() {
        successCommunication.map(true)
        stateCommunication.hideProgress()
    }

    fun stateFalse()= viewModelScope.launch {
        successCommunication.map(false)
        repository.signOut()
    }

    fun observeState(owner: LifecycleOwner,observer: Observer<SignUpStateCommunication.State>){
        stateCommunication.observe(owner, observer)
    }
}