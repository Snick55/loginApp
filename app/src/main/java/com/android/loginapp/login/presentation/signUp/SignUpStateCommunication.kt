package com.android.loginapp.login.presentation.signUp

import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.android.loginapp.R
import com.android.loginapp.login.model.EmptyFieldException
import com.android.loginapp.login.model.Field
import com.android.loginapp.login.model.NotEnoughCharsException
import com.android.loginapp.login.model.PasswordMismatchException
import java.lang.Exception

interface SignUpStateCommunication {


    fun map(e: Exception)

    fun observe(owner: LifecycleOwner, observer: Observer<State>)

    fun showProgress()

    fun hideProgress()

    class Base: SignUpStateCommunication{

        private val state = MutableLiveData(State())


        override fun map(e: Exception) {
            when(e){
                is PasswordMismatchException -> handlePasswordMismatch()
                is EmptyFieldException ->  handleEmptyFieldError(e)
                is NotEnoughCharsException -> handleNotEnoughCharsException()
                is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException -> {
                    handleInvalidEmailException()
                    hideProgress()
                }
                is com.google.firebase.auth.FirebaseAuthUserCollisionException -> handleAccountAlreadyExist()
            }
        }

        override fun observe(
            owner: LifecycleOwner,
            observer: Observer<State>
        ) {
           state.observe(owner, observer)
        }

        override fun showProgress() {
            state.value = State(signUpInProgress = true)
        }

        override fun hideProgress() {
            state.value = state.value?.copy(signUpInProgress = false)
        }

        private fun handleInvalidEmailException() {
            state.value = state.value?.copy(emailErrorMessageRes = R.string.invalid_email)
        }

        private fun handlePasswordMismatch() {
            state.value =
                state.value?.copy(repeatPasswordErrorMessageRes = R.string.password_missmatch)
            hideProgress()
        }

        private fun handleNotEnoughCharsException() {
            state.value = state.value?.copy(passwordErrorMessageRes = R.string.not_enough_chars)
            hideProgress()
        }

        private fun handleEmptyFieldError(e: EmptyFieldException) {
            state.value = when (e.field) {
                Field.EMAIL -> state.value
                    ?.copy(emailErrorMessageRes = R.string.field_is_empty)
                Field.NAME -> state.value
                    ?.copy(usernameErrorMessageRes = R.string.field_is_empty)
                Field.PASSWORD -> state.value
                    ?.copy(passwordErrorMessageRes = R.string.field_is_empty)
            }
            hideProgress()
        }

        private fun handleAccountAlreadyExist(){
            state.value = state.value?.copy(emailErrorMessageRes = R.string.account_already_exist)
        }
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

}