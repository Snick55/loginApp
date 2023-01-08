package com.android.loginapp.model

import java.lang.Exception

sealed class Result{



    object Success : Result()
    object Load : Result()

    class Failure(val exception: Exception): Result(){

    }


}
