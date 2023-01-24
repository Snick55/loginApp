package com.android.loginapp.core

import android.app.Application
import android.content.Context

import com.android.loginapp.login.model.AuthService
import com.android.loginapp.login.model.LoginRepository
import com.android.loginapp.login.model.Validator
import com.android.loginapp.login.presentation.profile.ProfileViewModel
import com.android.loginapp.login.presentation.signIn.LoginStateCommunication
import com.android.loginapp.login.presentation.signIn.LoginSuccessCommunication
import com.android.loginapp.login.presentation.signIn.LoginViewModel
import com.android.loginapp.login.presentation.signUp.SignUpStateCommunication
import com.android.loginapp.login.presentation.signUp.SignUpSuccessCommunication
import com.android.loginapp.login.presentation.signUp.SignUpViewModel
import com.android.loginapp.login.presentation.splash.SplashViewModel
import com.android.loginapp.maps.MainViewModel
import com.android.loginapp.maps.model.PreferencesManager
import com.android.loginapp.maps.presentation.MapsViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yandex.mapkit.MapKitFactory



 class App : Application() {

    lateinit var viewModel: SignUpViewModel
    lateinit var splashViewModel: SplashViewModel
    lateinit var loginViewModel: LoginViewModel
    lateinit var profileViewModel: ProfileViewModel
    lateinit var mainViewModel: MainViewModel
    lateinit var mapsViewModel: MapsViewModel

    override fun onCreate() {
        super.onCreate()


        MapKitFactory.setApiKey("02a9631c-7de2-40a3-8d00-753b6428da83")

        val auth = Firebase.auth
        val validator = Validator.Base()
        val authService = AuthService.Base(auth)
        val repository = LoginRepository.Base(authService, validator)
        val loginSuccessCommunication = LoginSuccessCommunication.Base()
        val loginStateCommunication = LoginStateCommunication.Base()
        val signUpSuccessCommunication = SignUpSuccessCommunication.Base()
        val signUpStateCommunication = SignUpStateCommunication.Base()
        val sharedPreferences = getSharedPreferences(APP_PREF,Context.MODE_PRIVATE)
        val sharedPreferencesManager = PreferencesManager.Base(sharedPreferences)

        loginViewModel = LoginViewModel(repository,loginSuccessCommunication,loginStateCommunication)
        viewModel = SignUpViewModel(repository,signUpSuccessCommunication,signUpStateCommunication)
        profileViewModel = ProfileViewModel(repository)
        splashViewModel = SplashViewModel(repository)
        mainViewModel= MainViewModel(sharedPreferencesManager)
        mapsViewModel = MapsViewModel(sharedPreferencesManager,repository)
    }

     companion object{
         private const val APP_PREF = "APP_PREF"
     }

}