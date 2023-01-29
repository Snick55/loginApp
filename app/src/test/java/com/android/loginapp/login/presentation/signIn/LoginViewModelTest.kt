package com.android.loginapp.login.presentation.signIn

import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.android.loginapp.login.model.*
import com.android.loginapp.login.presentation.AuthCallback
import com.android.loginapp.maps.presentation.SuccessRequestCallback
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.Exception
import java.net.UnknownHostException

class LoginViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("Ui ")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    private val emptyEmailFieldException = EmptyFieldException(Field.EMAIL)
    private val emptyPassFieldException = EmptyFieldException(Field.PASSWORD)
    private val notEnoughCharsException = NotEnoughCharsException()


    @Test
    fun `testing state false method`() {
        val loginSuccessCommunication = LoginSuccessCommunicationTest()
        val stateCommunication = LoginStateCommunicationTest()
        val repository = RepositoryTest(isSuccess = true, exception = notEnoughCharsException)


        val viewModel = LoginViewModel(repository, loginSuccessCommunication, stateCommunication)
        viewModel.stateFalse()
        val actual = loginSuccessCommunication.liveData
        val expected = false

        assertEquals(expected, actual)
    }

    @Test
    fun `testing sign in method with success conditionals`() = runBlocking {
        val loginSuccessCommunication = LoginSuccessCommunicationTest()
        val stateCommunication = LoginStateCommunicationTest()
        val repository = RepositoryTest(isSuccess = true, exception = notEnoughCharsException)
        val viewModel = LoginViewModel(repository, loginSuccessCommunication, stateCommunication)
        val email = "email@gmail.com"
        val password = "123456"
        async(Dispatchers.Main) {
            viewModel.signIn(email, password)
        }.await()
        val actual = loginSuccessCommunication.liveData
        val expected = true
        assertEquals(expected, actual)
    }

    @Test
    fun `testing sign in method with emptyPassFieldException error conditionals`() = runBlocking {
        val loginSuccessCommunication = LoginSuccessCommunicationTest()
        val stateCommunication = LoginStateCommunicationTest()
        val repository = RepositoryTest(isSuccess = false, exception = emptyPassFieldException)
        val viewModel = LoginViewModel(repository, loginSuccessCommunication, stateCommunication)
        val email = "email@gmail.com"
        val password = "123456"
        async(Dispatchers.Main) {
            viewModel.signIn(email, password)
        }.await()
        val actual = stateCommunication.state.passwordErrorMessageRes
        val expected = 6
        assertEquals(expected, actual)
    }

    @Test
    fun `testing sign in method with notEnoughCharsException error conditionals`() = runBlocking {
        val loginSuccessCommunication = LoginSuccessCommunicationTest()
        val stateCommunication = LoginStateCommunicationTest()
        val repository = RepositoryTest(isSuccess = false, exception = notEnoughCharsException)
        val viewModel = LoginViewModel(repository, loginSuccessCommunication, stateCommunication)
        val email = "email@gmail.com"
        val password = "123456"
        async(Dispatchers.Main) {
            viewModel.signIn(email, password)
        }.await()
        val actual = stateCommunication.state.passwordErrorMessageRes
        val expected = 5
        assertEquals(expected, actual)
    }

    @Test
    fun `testing sign in method with emptyEmailFieldException error conditionals`() = runBlocking {
        val loginSuccessCommunication = LoginSuccessCommunicationTest()
        val stateCommunication = LoginStateCommunicationTest()
        val repository = RepositoryTest(isSuccess = false, exception = emptyEmailFieldException)
        val viewModel = LoginViewModel(repository, loginSuccessCommunication, stateCommunication)
        val email = "email@gmail.com"
        val password = "123456"
        async(Dispatchers.Main) {
            viewModel.signIn(email, password)
        }.await()
        val actual = stateCommunication.state.emailErrorMessageRes
        val expected = 6
        assertEquals(expected, actual)
    }



    @Test
    fun `testing sign in method with FirebaseInvalidUser error conditionals`() = runBlocking {
        val loginSuccessCommunication = LoginSuccessCommunicationTest()
        val stateCommunication = LoginStateCommunicationTest()
        val exception = FirebaseInvalidUser()
        val repository = RepositoryTest(isSuccess = false, exception = exception)
        val viewModel = LoginViewModel(repository, loginSuccessCommunication, stateCommunication)
        val email = "email@gmail.com"
        val password = "123456"
        async(Dispatchers.Main) {
            viewModel.signIn(email, password)
        }.await()
        val actual = stateCommunication.state.emailErrorMessageRes
        val expected = 2
        assertEquals(expected, actual)
    }

    @Test
    fun `testing sign in method with FirebaseAuthInvalidCredentials error conditionals`() = runBlocking {
        val loginSuccessCommunication = LoginSuccessCommunicationTest()
        val stateCommunication = LoginStateCommunicationTest()
        val exception = FirebaseAuthInvalidCredentials()
        val repository = RepositoryTest(isSuccess = false, exception = exception)
        val viewModel = LoginViewModel(repository, loginSuccessCommunication, stateCommunication)
        val email = "email@gmail.com"
        val password = "123456"
        async(Dispatchers.Main) {
            viewModel.signIn(email, password)
        }.await()
        val actual = stateCommunication.state.emailErrorMessageRes
        val expected = 4
        assertEquals(expected, actual)
    }

    @Test
    fun `testing sign in method with FirebaseAuthUserCollision error conditionals`() = runBlocking {
        val loginSuccessCommunication = LoginSuccessCommunicationTest()
        val stateCommunication = LoginStateCommunicationTest()
        val exception = FirebaseAuthUserCollision()
        val repository = RepositoryTest(isSuccess = false, exception = exception)
        val viewModel = LoginViewModel(repository, loginSuccessCommunication, stateCommunication)
        val email = "email@gmail.com"
        val password = "123456"
        async(Dispatchers.Main) {
            viewModel.signIn(email, password)
        }.await()
        val actual = stateCommunication.state.emailErrorMessageRes
        val expected = 1
        assertEquals(expected, actual)
    }

  @Test
    fun `testing sign in method with GenericException error conditionals`() = runBlocking {
        val loginSuccessCommunication = LoginSuccessCommunicationTest()
        val stateCommunication = LoginStateCommunicationTest()
        val exception = UnknownHostException()
        val repository = RepositoryTest(isSuccess = false, exception = exception)
        val viewModel = LoginViewModel(repository, loginSuccessCommunication, stateCommunication)
        val email = "email@gmail.com"
        val password = "123456"
        async(Dispatchers.Main) {
            viewModel.signIn(email, password)
        }.await()
        val actual = stateCommunication.state.emailErrorMessageRes
        val expected = 3
        assertEquals(expected, actual)
    }

     class LoginSuccessCommunicationTest : LoginSuccessCommunication {

        var liveData = false

        override fun map(result: Boolean) {
            liveData = result
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<Boolean>) {

        }
    }

    class LoginStateCommunicationTest : LoginStateCommunication {

        var state = LoginStateCommunication.State()


        override fun map(e: Exception) {
            when (e) {
                is EmptyFieldException -> handleEmptyFieldError(e)
                is NotEnoughCharsException -> handleNotEnoughCharsException()
                is FirebaseAuthInvalidCredentials -> {
                    handleInvalidEmailException()
                }
                is FirebaseInvalidUser -> {
                    handleInvalidUserException()
                }
                is FirebaseAuthUserCollision -> handleAccountAlreadyExist()
                else -> handleGenericException()
            }
        }

        override fun observe(
            owner: LifecycleOwner,
            observer: Observer<LoginStateCommunication.State>
        ) {

        }


        private fun handleAccountAlreadyExist() {
            state = state.copy(emailErrorMessageRes = 1)
        }


        private fun handleInvalidUserException() {
            state = state.copy(emailErrorMessageRes = 2)
            hideProgress()
        }


        private fun handleGenericException() {
            state = state.copy(emailErrorMessageRes = 3)
            hideProgress()
        }


        private fun handleInvalidEmailException() {
            state = state.copy(emailErrorMessageRes = 4)
            hideProgress()
        }

        private fun handleNotEnoughCharsException() {
            state = state.copy(passwordErrorMessageRes = 5)
            hideProgress()
        }

        private fun handleEmptyFieldError(e: EmptyFieldException) {
            state = when (e.field) {
                Field.EMAIL -> state.copy(emailErrorMessageRes = 6)
                Field.NAME -> throw IllegalStateException()
                Field.PASSWORD -> state.copy(passwordErrorMessageRes = 6)
            }
            hideProgress()
        }

        override fun showProgress() {
            state = LoginStateCommunication.State(signInInProgress = true)
        }

        override fun hideProgress() {
            state = state.copy(signInInProgress = false)
        }


        data class State(
            @StringRes val emailErrorMessageRes: Int = 0,
            @StringRes val passwordErrorMessageRes: Int = 0,
            val signInInProgress: Boolean = false,
        ) {
            val showProgress: Boolean get() = signInInProgress
            val enableViews: Boolean get() = !signInInProgress
        }


    }

     class RepositoryTest(val isSuccess: Boolean = true, val exception: Exception) :
        LoginRepository {

        override suspend fun changeName(name: String, callback: SuccessRequestCallback) {
            callback.success()
        }

        override suspend fun getName(): String {
            return "name"
        }

        override suspend fun isSignIn(): Boolean {
            if (isSuccess) return true
            else return false
        }

        override suspend fun signIn(email: String, password: String, callback: AuthCallback) {
            if (isSuccess) {
                callback.success()
            } else callback.map(exception)
        }

        override suspend fun signUp(
            name: String,
            email: String,
            password: String,
            repeatPass: String,
            callback: AuthCallback
        ) {
            if (isSuccess)
                callback.success()
            else callback.map(exception)
        }

        override suspend fun signOut() {

        }
    }

    class FirebaseInvalidUser: Exception()
    class FirebaseAuthUserCollision: Exception()
    class FirebaseAuthInvalidCredentials: Exception()

}