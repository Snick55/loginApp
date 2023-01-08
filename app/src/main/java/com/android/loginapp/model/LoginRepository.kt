package com.android.loginapp.model

import android.util.Log
import com.android.loginapp.presentation.profile.SuccessRequestCallback
import com.android.loginapp.presentation.signIn.LoginViewModel
import com.android.loginapp.presentation.signUp.SignUpViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest

interface LoginRepository {

    suspend fun changeName(name: String,callback: SuccessRequestCallback)

    suspend fun currentUser(): String

    suspend fun isSignIn(): Boolean

    suspend fun signIn(email: String, password: String,callback: LoginViewModel.ErrorHandler)

    suspend fun signUp(name: String, email: String, password: String, repeatPass: String,callback: SignUpViewModel.ErrorHandler)

    suspend fun signOut()


    class Base(
        private val auth: FirebaseAuth,
        private val validator: Validator
    ) : LoginRepository {


        override suspend fun changeName(name: String,callback:SuccessRequestCallback) {
            val request = userProfileChangeRequest {
                displayName = name
            }

            auth.currentUser!!.updateProfile(request)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        callback.success()
                    }
                }
                .addOnFailureListener {
                    Log.d("TAG","ERROR UPDATE REQUEST: $it")
                }

        }

        override suspend fun signOut() {
            auth.signOut()
            Log.d(
                "TAG",
                "after sign out value of currentUser = ${currentUser()},${auth.currentUser}  "
            )
        }

        override suspend fun currentUser(): String {
            val currentUser = auth.currentUser
            Log.d("TAG", "current email is ${currentUser?.displayName}")
            return currentUser?.displayName ?: ""

        }

        override suspend fun isSignIn(): Boolean {
            return auth.currentUser != null
        }


        override suspend fun signIn( email: String, password: String,callback: LoginViewModel.ErrorHandler) {
            validator.validate(email,password,password)

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("TAG", "SIGN IN SUCCESS")
                        callback.success()
                    }
                }
                .addOnFailureListener {
                    Log.d("TAG","SIGN IN EXCEPTION IS $it")
                    callback.map(it)
                }

        }

        override suspend fun signUp(
            name: String,
            email: String,
            password: String,
            repeatPass: String,
            callback: SignUpViewModel.ErrorHandler
        ) {
            validator.validate(email, password, repeatPass)
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("TAG", "User was Created")
                        val profileUpdate= userProfileChangeRequest {
                            displayName = name
                        }
                        auth.currentUser!!.updateProfile(profileUpdate)
                            .addOnCompleteListener{
                                callback.success()
                            }
                            .addOnFailureListener {
                            Log.d("TAG","ERROR UPDATE USER PFOFILE: $it")
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