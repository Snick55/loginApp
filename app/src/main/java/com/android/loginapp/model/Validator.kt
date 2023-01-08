package com.android.loginapp.model

import android.util.Patterns

interface Validator {

    fun validate(email: String,password: String, repeatPassword: String)


    class Base: Validator{

        override fun validate(email: String, password: String, repeatPassword: String) {
            if (email.isBlank()) throw EmptyFieldException(Field.EMAIL)
            if (password.isBlank()) throw EmptyFieldException(Field.PASSWORD)
            if (password.length < 6) throw NotEnoughCharsException()
            if (repeatPassword.isBlank()) throw EmptyFieldException(Field.PASSWORD)
            if (password != repeatPassword) throw PasswordMismatchException()

        }
    }

}