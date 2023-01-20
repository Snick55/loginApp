package com.android.loginapp.presentation.signIn

import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.android.loginapp.R
import com.android.loginapp.model.EmptyFieldException
import com.android.loginapp.model.Field
import com.android.loginapp.model.NotEnoughCharsException
import com.android.loginapp.presentation.signUp.SignUpViewModel
import java.lang.Exception

interface LoginStateCommunication {

    fun map(e: Exception)

    fun observe(owner: LifecycleOwner,observer: Observer<State>)

    fun showProgress()

    fun hideProgress()


    class Base: LoginStateCommunication{

        private val state = MutableLiveData(State())

        override fun map(e: Exception) {
            when(e){
               is EmptyFieldException ->  handleEmptyFieldError(e)
                is NotEnoughCharsException -> handleNotEnoughCharsException()
                is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException -> {
                    handleInvalidEmailException()
                }
                is com.google.firebase.auth.FirebaseAuthInvalidUserException -> {
                    handleInvalidUserException()
                }
                is com.google.firebase.auth.FirebaseAuthUserCollisionException -> handleAccountAlreadyExist()
                else -> handleGenericException()
            }
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<State>) {
            state.observe(owner, observer)
        }


        private fun handleAccountAlreadyExist(){
            state.value = state.value?.copy(emailErrorMessageRes = R.string.account_already_exist)
        }


        private fun handleInvalidUserException() {
            state.value = state.value?.copy(emailErrorMessageRes = R.string.no_such_user)
            hideProgress()
        }


        private fun handleGenericException(){
            state.value = state.value?.copy(emailErrorMessageRes = R.string.someting_went_wrong)
        }




        private fun handleInvalidEmailException() {
            state.value = state.value?.copy(emailErrorMessageRes = R.string.invalid_email_or_password)
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
                Field.NAME -> throw IllegalStateException()
                Field.PASSWORD -> state.value
                    ?.copy(passwordErrorMessageRes = R.string.field_is_empty)
            }
            hideProgress()
        }

         override fun showProgress() {
            state.value = State(signInInProgress = true)
        }

         override fun hideProgress() {
            state.value = state.value?.copy(signInInProgress = false)
        }

    }

    data class State(
        @StringRes val emailErrorMessageRes: Int = SignUpViewModel.NO_ERROR_MESSAGE,
        @StringRes val passwordErrorMessageRes: Int = SignUpViewModel.NO_ERROR_MESSAGE,
        private val signInInProgress: Boolean = false,
    ) {
        val showProgress: Boolean get() = signInInProgress
        val enableViews: Boolean get() = !signInInProgress
    }

}