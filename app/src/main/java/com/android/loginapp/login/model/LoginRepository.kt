package com.android.loginapp.login.model

import com.android.loginapp.login.presentation.AuthCallback
import com.android.loginapp.login.presentation.profile.SuccessRequestCallback

interface LoginRepository {

    suspend fun changeName(name: String, callback: SuccessRequestCallback)

    suspend fun getName(): String

    suspend fun isSignIn(): Boolean

    suspend fun signIn(email: String, password: String, callback: AuthCallback)

    suspend fun signUp(
        name: String,
        email: String,
        password: String,
        repeatPass: String,
        callback: AuthCallback
    )

    suspend fun signOut()


    class Base(
        private val authService: AuthService,
        private val validator: Validator
    ) : LoginRepository {


        override suspend fun changeName(name: String, callback: SuccessRequestCallback) {
            authService.changeName(name, callback)
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


        override suspend fun signIn(
            email: String,
            password: String,
            callback: AuthCallback
        ) {
            validator.validate(email, password, password)
            authService.signIn(email, password, callback)
        }

        override suspend fun signUp(
            name: String,
            email: String,
            password: String,
            repeatPass: String,
            callback: AuthCallback
        ) {
            validator.validate(email, password, repeatPass)
            authService.createUser(name, email, password, callback)
        }
    }


}