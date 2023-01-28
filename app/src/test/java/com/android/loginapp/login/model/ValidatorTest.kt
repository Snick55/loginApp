package com.android.loginapp.login.model

import org.junit.Assert.*
import org.junit.Test

class ValidatorTest {

    private val emptyEmailFieldException = EmptyFieldException(Field.EMAIL)
    private val emptyPassFieldException = EmptyFieldException(Field.PASSWORD)
    private val emptyRepeatPassFieldException = EmptyFieldException(Field.PASSWORD)
    private val notEnoughCharsException = NotEnoughCharsException()

    private val mismatchException = PasswordMismatchException()

    @Test
    fun validate_empty_email_field() {
        val validator = Validator.Base()
        val email = ""
        val password = "123456"
        val repeatPassword = "123456"

        try {
            validator.validate(email, password, repeatPassword)
        } catch (e: AppException) {
            assertEquals(e::class, emptyEmailFieldException::class)
        }

    }

    @Test
    fun validate_empty_pass_field() {
        val validator = Validator.Base()
        val email = "someemail@mail.ru"
        val password = ""
        val repeatPassword = "123456"
        try {
            validator.validate(email, password, repeatPassword)
        } catch (e: AppException) {
            assertEquals(e::class, emptyPassFieldException::class)
        }
    }

    @Test
    fun validate_empty_reap_pass_field() {
        val validator = Validator.Base()
        val email = "someemail@mail.ru"
        val password = "123456"
        val repeatPassword = ""
        try {
            validator.validate(email, password, repeatPassword)
        } catch (e: AppException) {
            assertEquals(e::class, emptyRepeatPassFieldException::class)
        }
    }


    @Test
    fun validate_pass_mismatch() {
        val validator = Validator.Base()
        val email = "someemail@mail.ru"
        val password = "123456"
        val repeatPassword = "123445"
        try {
            validator.validate(email, password, repeatPassword)
        } catch (e: AppException) {
            assertEquals(e::class, mismatchException::class)
        }
    }


    @Test
    fun validate_not_enough_symbols() {
        val validator = Validator.Base()
        val email = "someemail@mail.ru"
        val password = "123"
        val repeatPassword = "123"
        try {
            validator.validate(email, password, repeatPassword)
        } catch (e: AppException) {
            assertEquals(e::class, notEnoughCharsException::class)
        }
    }


    @Test
    fun `if email and pass is empty`() {
        val validator = Validator.Base()
        val email = ""
        val password = ""
        val repeatPassword = ""
        try {
            validator.validate(email, password, repeatPassword)
        } catch (e: AppException) {
            assertEquals(e::class, emptyEmailFieldException::class)
        }
    }

    @Test
    fun `if all fields is good`() {
        val validator = Validator.Base()
        val email = "someemail@mail.ru"
        val password = "123456"
        val repeatPassword = "123456"
        val expected = true
        val actual = try {
            validator.validate(email, password, repeatPassword)
            true
        } catch (e: AppException) {
            assertEquals(e::class, emptyEmailFieldException::class)
            false
        }
        assertEquals(expected, actual)
    }

}