package com.android.loginapp.core

import android.app.Application
import com.android.loginapp.model.AuthService
import com.android.loginapp.model.LoginRepository
import com.android.loginapp.model.Validator
import com.android.loginapp.presentation.profile.ProfileViewModel
import com.android.loginapp.presentation.signIn.LoginViewModel
import com.android.loginapp.presentation.signUp.SignUpViewModel
import com.android.loginapp.presentation.splash.SplashViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

 class App : Application() {

    lateinit var viewModel: SignUpViewModel
    lateinit var splashViewModel: SplashViewModel
    lateinit var loginViewModel: LoginViewModel
    lateinit var profileViewModel: ProfileViewModel

    override fun onCreate() {
        super.onCreate()

        val auth = Firebase.auth
        val validator = Validator.Base()
        val authService = AuthService.Base(auth)
        val repository = LoginRepository.Base(authService, validator)

        loginViewModel = LoginViewModel(repository)
        viewModel = SignUpViewModel(repository)
        profileViewModel = ProfileViewModel(repository)
        splashViewModel = SplashViewModel(repository)
    }

}