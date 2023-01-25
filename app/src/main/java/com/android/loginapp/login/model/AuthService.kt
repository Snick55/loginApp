package com.android.loginapp.login.model

import com.android.loginapp.login.presentation.AuthCallback
import com.android.loginapp.maps.presentation.SuccessRequestCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface AuthService {

    suspend fun changeName(name: String, callback: SuccessRequestCallback)
    suspend fun getName(): String
    suspend fun isSignIn(): Boolean
    suspend fun signOut()
    suspend fun signIn(email: String, password: String, callback: AuthCallback)

    suspend fun createUser(
        name: String,
        email: String,
        password: String,
        callback: AuthCallback
    )

    class Base(private val auth: FirebaseAuth) : AuthService {
        override suspend fun changeName(
            name: String,
            callback: SuccessRequestCallback
        ) {
            val request = userProfileChangeRequest {
                displayName = name
            }
            withContext(Dispatchers.IO) {
                auth.currentUser!!.updateProfile(request)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            callback.success()
                        }
                    }
            }
        }

        override suspend fun getName(): String = withContext(Dispatchers.IO) {
            val currentUser = auth.currentUser
            return@withContext currentUser?.displayName ?: ""
        }


        override suspend fun isSignIn(): Boolean = withContext(Dispatchers.IO) {
                return@withContext auth.currentUser != null
            }

        override suspend fun signOut() {
            withContext(Dispatchers.IO) {
                auth.signOut()
            }
        }

        override suspend fun signIn(
            email: String,
            password: String,
            callback: AuthCallback
        ) {
            withContext(Dispatchers.IO) { // TODO: fix hardcode dispatchers
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
        }

        override suspend fun createUser(
            name: String,
            email: String,
            password: String,
            callback: AuthCallback
        ) {
            withContext(Dispatchers.IO) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val profileUpdate = userProfileChangeRequest {
                                displayName = name
                            }
                            auth.currentUser!!.updateProfile(profileUpdate)
                                .addOnCompleteListener {
                                    callback.success()
                                }
                                .addOnFailureListener {
                                    callback.map(it)
                                }
                        }else if(task.exception != null) callback.map(task.exception!!)
                    }
                    .addOnFailureListener {
                        callback.map(it)
                    }
            }
        }
    }
}