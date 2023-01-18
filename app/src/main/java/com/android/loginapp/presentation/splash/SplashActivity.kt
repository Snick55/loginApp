package com.android.loginapp.presentation.splash

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.loginapp.MainActivity
import com.android.loginapp.R
import com.android.loginapp.core.App
import com.android.loginapp.databinding.ActivitySplashBinding
import com.android.loginapp.presentation.profile.FragmentProfile
import com.android.loginapp.presentation.signIn.LoginFragment


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val viewModel = (this.applicationContext as App).splashViewModel
        viewModel.isSignIn()
        viewModel.isSignIn.observe(this) {
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("isSignIn",it)
            startActivity(intent)
            finish()
        }


    }
}