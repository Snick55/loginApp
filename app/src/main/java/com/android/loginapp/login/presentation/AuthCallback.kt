package com.android.loginapp.login.presentation

import java.lang.Exception

interface AuthCallback {

    fun map(e: Exception)
    fun success()

}