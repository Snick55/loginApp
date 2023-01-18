package com.android.loginapp.model

import com.android.loginapp.presentation.profile.SuccessRequestCallback
import com.android.loginapp.presentation.signIn.LoginViewModel
import com.android.loginapp.presentation.signUp.SignUpViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.userProfileChangeRequest

interface AuthService {

    suspend fun changeName(name: String, callback: SuccessRequestCallback)
    suspend fun getName(): String
    suspend fun isSignIn(): Boolean
    suspend fun signOut()
    suspend fun signIn(email: String, password: String, callback: LoginViewModel.ErrorHandler)

    suspend fun createUser(
        name: String,
        email: String,
        password: String,
        callback: SignUpViewModel.ErrorHandler
    )

    class Base(private val auth: FirebaseAuth) : AuthService {
        override suspend fun changeName(
            name: String,
            callback: SuccessRequestCallback
        ) {
            val request = userProfileChangeRequest {
                displayName = name
            }
            auth.currentUser!!.updateProfile(request)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        callback.success()
                    }
                }
        }

        override suspend fun getName(): String {
            val currentUser = auth.currentUser
            return currentUser?.displayName ?: ""
        }

        override suspend fun isSignIn(): Boolean {
            return auth.currentUser != null
        }

        override suspend fun signOut() {
            auth.signOut()
        }

        override suspend fun signIn(
            email: String,
            password: String,
            callback: LoginViewModel.ErrorHandler
        ) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        callback.success()
                    }
                }
                .addOnFailureListener {
                    callback.map(it)
                }
        }

        override suspend fun createUser(
            name: String,
            email: String,
            password: String,
            callback: SignUpViewModel.ErrorHandler
        ) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val profileUpdate= userProfileChangeRequest {
                            displayName = name
                        }
                        auth.currentUser!!.updateProfile(profileUpdate)
                            .addOnCompleteListener{
                                callback.success()
                            }
                            .addOnFailureListener {
                                callback.map(it)
                            }
                    }

                }
                .addOnFailureListener {
                    callback.map(it)
                }
        }
    }
}