package com.android.loginapp.model

import com.android.loginapp.presentation.profile.SuccessRequestCallback
import com.android.loginapp.presentation.signIn.LoginViewModel
import com.android.loginapp.presentation.signUp.SignUpViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest

interface LoginRepository {

    suspend fun changeName(name: String,callback: SuccessRequestCallback)

    suspend fun getName(): String

    suspend fun isSignIn(): Boolean

    suspend fun signIn(email: String, password: String,callback: LoginViewModel.ErrorHandler)

    suspend fun signUp(name: String, email: String, password: String, repeatPass: String,callback: SignUpViewModel.ErrorHandler)

    suspend fun signOut()


    class Base(
        private val authService: AuthService,
        private val validator: Validator
    ) : LoginRepository {


        override suspend fun changeName(name: String,callback:SuccessRequestCallback) {

            authService.changeName(name,callback)
        }

        override suspend fun signOut() {
            authService.signOut()
        }

        override suspend fun getName(): String {
           return authService.getName()


        }

        override suspend fun isSignIn(): Boolean {
           return authService.isSignIn()

        }


        override suspend fun signIn( email: String, password: String,callback: LoginViewModel.ErrorHandler) {
            validator.validate(email,password,password)

            authService.signIn(email,password,callback)


        }

        override suspend fun signUp(
            name: String,
            email: String,
            password: String,
            repeatPass: String,
            callback: SignUpViewModel.ErrorHandler
        ) {
            validator.validate(email, password, repeatPass)
            authService.createUser(name,email,password,callback)

        }
    }


}