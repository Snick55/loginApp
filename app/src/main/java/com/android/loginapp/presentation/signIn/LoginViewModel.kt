package com.android.loginapp.presentation.signIn

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.loginapp.R
import com.android.loginapp.model.EmptyFieldException
import com.android.loginapp.model.Field
import com.android.loginapp.model.LoginRepository
import com.android.loginapp.model.NotEnoughCharsException
import com.android.loginapp.presentation.signUp.SignUpViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModel(
    private val repository: LoginRepository
) : ViewModel() {

    private val _successLiveData = MutableLiveData(false)
    val successLiveData: LiveData<Boolean> = _successLiveData

    private val _state = MutableLiveData(State())
    val state: LiveData<State> = _state

    private val callback = object : ErrorHandler {
        override fun map(e: Exception) {
            when (e) {
                is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException -> {
                    handleInvalidEmailException()
                }
                is com.google.firebase.auth.FirebaseAuthInvalidUserException -> {
                    handleInvalidUserException()
                }

            }

        }

        override fun success() {
            Log.d("TAG", "block SUCCESS")
            _successLiveData.value = true
            hideProgress()

        }
    }

    private fun handleInvalidUserException() {
        _state.value = _state.value?.copy(emailErrorMessageRes = R.string.no_such_user)
        hideProgress()
    }


    fun signIn(email: String, password: String) = viewModelScope.launch {
        showProgress()
        try {
            repository.signIn(email, password, callback)
        } catch (e: EmptyFieldException) {
            handleEmptyFieldError(e)
        } catch (e: NotEnoughCharsException) {
            handleNotEnoughCharsException()
        }

    }


    private fun handleInvalidEmailException() {
        _state.value = _state.value?.copy(emailErrorMessageRes = R.string.invalid_email_or_password)
        hideProgress()
    }

    private fun handleNotEnoughCharsException() {
        _state.value = _state.value?.copy(passwordErrorMessageRes = R.string.not_enough_chars)
        hideProgress()
    }

    private fun handleEmptyFieldError(e: EmptyFieldException) {
        _state.value = when (e.field) {
            Field.EMAIL -> _state.value
                ?.copy(emailErrorMessageRes = R.string.field_is_empty)
            Field.NAME -> throw IllegalStateException()
            Field.PASSWORD -> _state.value
                ?.copy(passwordErrorMessageRes = R.string.field_is_empty)
        }
        hideProgress()
    }

    private fun showProgress() {
        _state.value = State(signInInProgress = true)
    }

    private fun hideProgress() {
        _state.value = _state.value?.copy(signInInProgress = false)
    }


    fun stateFalse() {
        _successLiveData.value = false
    }

    data class State(
        @StringRes val emailErrorMessageRes: Int = SignUpViewModel.NO_ERROR_MESSAGE,
        @StringRes val passwordErrorMessageRes: Int = SignUpViewModel.NO_ERROR_MESSAGE,
        private val signInInProgress: Boolean = false,
    ) {
        val showProgress: Boolean get() = signInInProgress
        val enableViews: Boolean get() = !signInInProgress
    }

    interface ErrorHandler {
        fun map(e: Exception)
        fun success()
    }

}