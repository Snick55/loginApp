package com.android.loginapp.login.model



open class AppException : RuntimeException()
 class EmptyFieldException(
    val field: Field
) : AppException()
class PasswordMismatchException : AppException()
class NotEnoughCharsException: AppException()

