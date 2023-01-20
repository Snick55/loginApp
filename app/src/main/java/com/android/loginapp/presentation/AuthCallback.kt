package com.android.loginapp.presentation

import java.lang.Exception

interface AuthCallback {

    fun map(e: Exception)
    fun success()

}