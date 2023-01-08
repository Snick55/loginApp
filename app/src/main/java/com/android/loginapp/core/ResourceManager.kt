package com.android.loginapp.core

import android.content.Context
import androidx.annotation.StringRes

interface ResourceManager {

    fun getString(@StringRes id: Int): String


    class Base(private val context: Context): ResourceManager{
        override fun getString(id: Int): String {
           return context.getString(id)
        }
    }


}