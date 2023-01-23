package com.android.loginapp.login.presentation.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.loginapp.MainActivity
import com.android.loginapp.R
import com.android.loginapp.core.App


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