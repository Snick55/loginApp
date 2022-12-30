package com.android.loginapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.loginapp.presentation.signIn.LoginFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null){
            val fragment = LoginFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container,fragment)
                .commit()
        }

    }
}