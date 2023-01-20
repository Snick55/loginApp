package com.android.loginapp.model

import com.android.loginapp.presentation.profile.SuccessRequestCallback
import com.android.loginapp.presentation.signIn.LoginViewModel
import com.android.loginapp.presentation.signUp.SignUpViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.lang.RuntimeException
import org.junit.Assert.*
import java.lang.Exception

class LoginRepositoryTest {


    private val exception = RuntimeException()


    @Test
    fun `getting name with success conditionals`(): Unit = runBlocking {
        val testValidator = TestValidator()
        val auth = TestFireBaseAuth(isSuccess = true,exception)
        val repository = LoginRepository.Base(
            auth,
            testValidator
        )

        val actual = repository.getName()
        val expected = "name"

        assertEquals(expected, actual)
    }


    @Test
    fun `getting name with error conditionals`(): Unit = runBlocking {
        val testValidator = TestValidator()
        val auth = TestFireBaseAuth(isSuccess = false,exception)
        val repository = LoginRepository.Base(
            auth,
            testValidator
        )

        val actual = repository.getName()
        val expected = ""

        assertEquals(expected, actual)
    }


    @Test
    fun `testing log out`(): Unit = runBlocking {
        val testValidator = TestValidator()
        val auth = TestFireBaseAuth(isSuccess = false,exception)
        val repository = LoginRepository.Base(
            auth,
            testValidator
        )
        val user = auth.currentUser
        val expectedUser = "default"

        assertEquals(expectedUser, user)

        repository.signOut()
        val actual = auth.isSignIn
        val expected = false

        assertEquals(expected, actual)
    }


    @Test
    fun `testing change name with error conditionals`(): Unit = runBlocking {
        val testValidator = TestValidator()
        val auth = TestFireBaseAuth(isSuccess = false,exception)
        val repository = LoginRepository.Base(
            auth,
            testValidator
        )
        val name = "name"
        val user = auth.currentUser
        val expectedUser = "default"

        assertEquals(expectedUser, user)

        repository.changeName(name, object : SuccessRequestCallback {
            override fun success() {
                auth.currentUser = name
            }
        })
        val actual = auth.currentUser

        assertEquals(expectedUser, actual)
    }

    @Test
    fun `testing change name with success conditionals`(): Unit = runBlocking {
        val testValidator = TestValidator()
        val auth = TestFireBaseAuth(isSuccess = true,exception)
        val repository = LoginRepository.Base(
            auth,
            testValidator
        )
        val name = "name"
        val user = auth.currentUser
        val expectedUser = "default"

        assertEquals(expectedUser, user)

        repository.changeName(name, object : SuccessRequestCallback {
            override fun success() {
                auth.currentUser = name
            }
        })
        val actual = auth.currentUser

        assertEquals(name, actual)
    }


    @Test
    fun `testing sign in method with success conditionals`(): Unit = runBlocking {
        val testValidator = TestValidator()
        val auth = TestFireBaseAuth(isSuccess = true,exception)
        val repository = LoginRepository.Base(
            auth,
            testValidator
        )
        val email = "ban34505@mail.ru"
        val password = "123456"
        var actual: Any? = null
        repository.signIn(email, password, object : LoginViewModel.ErrorHandler {
            override fun map(e: Exception) {
                actual = e
            }

            override fun success() {
                actual = true
            }
        })
        val expected = true

        assertEquals(expected, actual)
    }


    @Test
    fun `testing sign in method with error conditionals`(): Unit = runBlocking {
        val testValidator = TestValidator()
        val auth = TestFireBaseAuth(isSuccess = false,exception)
        val repository = LoginRepository.Base(
            auth,
            testValidator
        )
        val email = "ban34505@mail.ru"
        val password = "123456"
        var actual: Any? = null
        repository.signIn(email, password, object : LoginViewModel.ErrorHandler {
            override fun map(e: Exception) {
                actual = e
            }

            override fun success() {
                actual = true
            }
        })
        val expected = exception

        assertEquals(expected, actual)
    }


    @Test
    fun `testing sign out method after sign in`(): Unit = runBlocking {
        val testValidator = TestValidator()
        val auth = TestFireBaseAuth(isSuccess = true,exception)
        val repository = LoginRepository.Base(
            auth,
            testValidator
        )
        val email = "ban34505@mail.ru"
        val password = "123456"

        val actualBeforeSignIn = auth.isSignIn
        val expectedIsSignIn = false

        assertEquals(expectedIsSignIn,actualBeforeSignIn)

        var actualAfterSignIn: Any? = null
        repository.signIn(email, password, object : LoginViewModel.ErrorHandler {
            override fun map(e: Exception) {
                actualAfterSignIn = e
            }

            override fun success() {
                actualAfterSignIn = true
            }
        })
        val expectedAfterSignIn = true
        assertEquals(expectedAfterSignIn, actualAfterSignIn)

        repository.signOut()

        val actual = auth.isSignIn
        val expected = false

        assertEquals(expected, actual)

    }



    @Test
    fun `testing sign up method with success conditionals`(): Unit = runBlocking {
        val testValidator = TestValidator()

        val auth = TestFireBaseAuth(isSuccess = true,exception)
        val repository = LoginRepository.Base(
            auth,
            testValidator
        )
        val email = "ban34505@mail.ru"
        val password = "123456"
        val name = "Nick"
        var actual: Any? = null

        repository.signUp(name = name, email = email, password = password, repeatPass = password,
            object : SignUpViewModel.ErrorHandler {
                override fun map(e: Exception) {
                    actual = e
                }

                override fun success() {
                    actual = true
                }
            })

        val expected = true

        assertEquals(expected, actual)
    }


    @Test
    fun `testing sign up method with error conditionals`(): Unit = runBlocking {
        val testValidator = TestValidator()

        val auth = TestFireBaseAuth(isSuccess = false,exception)
        val repository = LoginRepository.Base(
            auth,
            testValidator
        )
        val email = "ban34505@mail.ru"
        val password = "123456"
        val name = "Nick"
        var actual: Any? = null

        repository.signUp(name = name, email = email, password = password, repeatPass = password,
            object : SignUpViewModel.ErrorHandler {
                override fun map(e: Exception) {
                    actual = e
                }

                override fun success() {
                    actual = true
                }
            })

        val expected = exception

        assertEquals(expected, actual)
    }







    class TestFireBaseAuth(
        private val isSuccess: Boolean = true,
        private val exception: RuntimeException
    ) : AuthService {

        var currentUser: String = "default"

        var isSignIn: Boolean = false

        override suspend fun changeName(
            name: String,
            callback: SuccessRequestCallback
        ) {
            if (isSuccess) {
                callback.success()
            }
        }

        override suspend fun getName(): String {
            return if (isSuccess) {
                currentUser = "name"
                return currentUser
            } else ""
        }

        override suspend fun isSignIn(): Boolean {
            return isSignIn
        }

        override suspend fun signOut() {
            isSignIn = false
        }

        override suspend fun signIn(
            email: String,
            password: String,
            callback: LoginViewModel.ErrorHandler
        ) {
            if (isSuccess) {
                callback.success()
                currentUser = "success"
            } else
                callback.map(exception)
        }

        override suspend fun createUser(
            name: String,
            email: String,
            password: String,
            callback: SignUpViewModel.ErrorHandler
        ) {
            if (isSuccess) {
                callback.success()
                currentUser = name
            } else callback.map(exception)

        }
    }

    class TestValidator : Validator {

        override fun validate(email: String, password: String, repeatPassword: String) {
            if (email.isBlank()) throw RuntimeException()
            if (password.isBlank()) throw RuntimeException()
            if (password.length < 6) throw RuntimeException()
            if (repeatPassword.isBlank()) throw RuntimeException()
            if (password != repeatPassword) throw RuntimeException()
        }
    }

}