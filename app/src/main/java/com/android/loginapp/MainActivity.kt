package com.android.loginapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.loginapp.login.presentation.signIn.LoginFragment
import com.android.loginapp.maps.presentation.MapsFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val isSignIn = intent.getBooleanExtra("isSignIn",false)


            if (isSignIn) {
                    val fragment = MapsFragment()
                    supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_container, fragment)
                        .commit()
                } else {
                    val fragment = LoginFragment()
                    supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_container, fragment)
                        .commit()
                }


    }
}