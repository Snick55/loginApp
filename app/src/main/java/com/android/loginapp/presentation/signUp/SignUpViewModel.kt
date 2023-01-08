package com.android.loginapp.presentation.signUp

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.loginapp.R
import com.android.loginapp.model.*
import kotlinx.coroutines.launch
import java.lang.Exception

class SignUpViewModel(
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
                    hideProgress()
                }
            }

        }

        override fun success() {
            _successLiveData.value = true
            hideProgress()

        }
    }


    fun signUp(name:String,email: String, password: String, repeatPass: String) =
        viewModelScope.launch {
            showProgress()
            try {
                repository.signUp(name,email, password, repeatPass, callback)
            } catch (e: PasswordMismatchException) {
                handlePasswordMismatch()
            } catch (e: EmptyFieldException) {
                handleEmptyFieldError(e)
            } catch (e: NotEnoughCharsException) {
                handleNotEnoughCharsException()
            }
        }

    private fun handleInvalidEmailException() {
        _state.value = _state.value?.copy(emailErrorMessageRes = R.string.invalid_email)
    }

    private fun handlePasswordMismatch() {
        _state.value =
            _state.value?.copy(repeatPasswordErrorMessageRes = R.string.password_missmatch)
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
            Field.NAME -> _state.value
                ?.copy(usernameErrorMessageRes = R.string.field_is_empty)
            Field.PASSWORD -> _state.value
                ?.copy(passwordErrorMessageRes = R.string.field_is_empty)
        }
        hideProgress()
    }

    private fun showProgress() {
        _state.value = State(signUpInProgress = true)
    }

    private fun hideProgress() {
        _state.value = _state.value?.copy(signUpInProgress = false)
    }

    data class State(
        @StringRes val emailErrorMessageRes: Int = NO_ERROR_MESSAGE,
        @StringRes val passwordErrorMessageRes: Int = NO_ERROR_MESSAGE,
        @StringRes val repeatPasswordErrorMessageRes: Int = NO_ERROR_MESSAGE,
        @StringRes val usernameErrorMessageRes: Int = NO_ERROR_MESSAGE,
        private val signUpInProgress: Boolean = false,
    ) {
        val showProgress: Boolean get() = signUpInProgress
        val enableViews: Boolean get() = !signUpInProgress
    }


    companion object {
        const val NO_ERROR_MESSAGE = 0
    }

    interface ErrorHandler {
        fun map(e: Exception)
        fun success()
    }

     fun stateFalse()= viewModelScope.launch {
        _successLiveData.value = false
         repository.signOut()
    }

}