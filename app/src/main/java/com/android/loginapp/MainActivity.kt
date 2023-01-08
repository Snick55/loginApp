package com.android.loginapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.loginapp.core.App
import com.android.loginapp.presentation.profile.FragmentProfile
import com.android.loginapp.presentation.signIn.LoginFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewModel = (this.applicationContext as App).mainViewModel
        viewModel.isSignIn()
        viewModel.isSignIn.observe(this){
          val isSignIn = it
            Log.d("TAG","isSignIn from viewModel observe = $isSignIn")
            if (savedInstanceState == null){
                Log.d("TAG","isSignIn = $isSignIn")
                if (isSignIn){
                    val fragment = FragmentProfile()
                    supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_container,fragment)
                        .commit()

                }else {
                    val fragment = LoginFragment()
                    supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_container,fragment)
                        .commit()
                }
        }





        }

    }
}