package com.android.loginapp.login.presentation.signUp

import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.android.loginapp.login.model.*
import com.android.loginapp.login.presentation.AuthCallback
import com.android.loginapp.login.presentation.signIn.LoginViewModelTest
import com.android.loginapp.maps.presentation.SuccessRequestCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.Exception

class SignUpViewModelTest{

    private val mainThreadSurrogate = newSingleThreadContext("Ui ")
    private val emptyEmailFieldException = EmptyFieldException(Field.EMAIL)
    private val emptyNameFieldException = EmptyFieldException(Field.NAME)
    private val emptyPassFieldException = EmptyFieldException(Field.PASSWORD)
    private val notEnoughCharsException = NotEnoughCharsException()
    private val passwordMismatchException = PasswordMismatchException()
    private val firebaseAuthUserCollision = LoginViewModelTest.FirebaseAuthUserCollision()
    private val firebaseAuthInvalidCredentials = LoginViewModelTest.FirebaseAuthInvalidCredentials()

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }


    @Test
    fun `testing state false method`() = runBlocking{
        val repository = RepositoryTest(isSuccess = true,emptyEmailFieldException)
        val successCommunicationTest = SignUpSuccessCommunicationTest()
        val stateCommunication = SignUpStateCommunicationTest()
        val signUpViewModel = SignUpViewModel(repository,successCommunicationTest,stateCommunication)
        async{ signUpViewModel.stateFalse() }.await()
        val actualSuccess = successCommunicationTest.liveData
        val expectedSuccess = false
        assertEquals(expectedSuccess,actualSuccess)
        val actual = repository.isSignIn
        val expected = false
        assertEquals(expected, actual)

    }

    @Test
    fun `testing signUp method with success conditionals`() = runBlocking{
        val repository = RepositoryTest(isSuccess = true,emptyEmailFieldException)
        val successCommunicationTest = SignUpSuccessCommunicationTest()
        val stateCommunication = SignUpStateCommunicationTest()
        val signUpViewModel = SignUpViewModel(repository,successCommunicationTest,stateCommunication)
        val email = "email@gmail.com"
        val name = "name"
        val password = "123456"
        val repeatPass = "123456"

        async(Dispatchers.Main){ signUpViewModel.signUp(name, email, password, repeatPass) }.await()

        val actual = successCommunicationTest.liveData
        val expected = true
        assertEquals(expected, actual)

    }

    @Test
    fun `testing signUp method with emptyEmailFieldException error`() = runBlocking{
        val repository = RepositoryTest(isSuccess = false,emptyEmailFieldException)
        val successCommunicationTest = SignUpSuccessCommunicationTest()
        val stateCommunication = SignUpStateCommunicationTest()
        val signUpViewModel = SignUpViewModel(repository,successCommunicationTest,stateCommunication)
        val email = "email@gmail.com"
        val name = "name"
        val password = "123456"
        val repeatPass = "123456"
        async(Dispatchers.Main){ signUpViewModel.signUp(name, email, password, repeatPass) }.await()

        val actual = stateCommunication.state.emailErrorMessageRes
        val expected = 6
        assertEquals(expected, actual)

    }


    @Test
    fun `testing signUp method with emptyPassFieldException error`() = runBlocking{
        val repository = RepositoryTest(isSuccess = false,emptyPassFieldException)
        val successCommunicationTest = SignUpSuccessCommunicationTest()
        val stateCommunication = SignUpStateCommunicationTest()
        val signUpViewModel = SignUpViewModel(repository,successCommunicationTest,stateCommunication)
        val email = "email@gmail.com"
        val name = "name"
        val password = "123456"
        val repeatPass = "123456"
        async(Dispatchers.Main){ signUpViewModel.signUp(name, email, password, repeatPass) }.await()

        val actual = stateCommunication.state.passwordErrorMessageRes
        val expected = 6
        assertEquals(expected, actual)

    }



    @Test
    fun `testing signUp method with emptyNameFieldException error`() = runBlocking{
        val repository = RepositoryTest(isSuccess = false,emptyNameFieldException)
        val successCommunicationTest = SignUpSuccessCommunicationTest()
        val stateCommunication = SignUpStateCommunicationTest()
        val signUpViewModel = SignUpViewModel(repository,successCommunicationTest,stateCommunication)
        val email = "email@gmail.com"
        val name = "name"
        val password = "123456"
        val repeatPass = "123456"
        async(Dispatchers.Main){ signUpViewModel.signUp(name, email, password, repeatPass) }.await()

        val actual = stateCommunication.state.usernameErrorMessageRes
        val expected = 6
        assertEquals(expected, actual)

    }


    @Test
    fun `testing signUp method with notEnoughCharsException error`() = runBlocking{
        val repository = RepositoryTest(isSuccess = false,notEnoughCharsException)
        val successCommunicationTest = SignUpSuccessCommunicationTest()
        val stateCommunication = SignUpStateCommunicationTest()
        val signUpViewModel = SignUpViewModel(repository,successCommunicationTest,stateCommunication)
        val email = "email@gmail.com"
        val name = "name"
        val password = "123456"
        val repeatPass = "123456"
        async(Dispatchers.Main){ signUpViewModel.signUp(name, email, password, repeatPass) }.await()

        val actual = stateCommunication.state.passwordErrorMessageRes
        val expected = 5
        assertEquals(expected, actual)

    }


    @Test
    fun `testing signUp method with passwordMismatchException error`() = runBlocking{
        val repository = RepositoryTest(isSuccess = false,passwordMismatchException)
        val successCommunicationTest = SignUpSuccessCommunicationTest()
        val stateCommunication = SignUpStateCommunicationTest()
        val signUpViewModel = SignUpViewModel(repository,successCommunicationTest,stateCommunication)
        val email = "email@gmail.com"
        val name = "name"
        val password = "123456"
        val repeatPass = "123456"
        async(Dispatchers.Main){ signUpViewModel.signUp(name, email, password, repeatPass) }.await()

        val actual = stateCommunication.state.repeatPasswordErrorMessageRes
        val expected = -1
        assertEquals(expected, actual)

    }


    @Test
    fun `testing signUp method with firebaseAuthUserCollision error`() = runBlocking{
        val repository = RepositoryTest(isSuccess = false,firebaseAuthUserCollision)
        val successCommunicationTest = SignUpSuccessCommunicationTest()
        val stateCommunication = SignUpStateCommunicationTest()
        val signUpViewModel = SignUpViewModel(repository,successCommunicationTest,stateCommunication)
        val email = "email@gmail.com"
        val name = "name"
        val password = "123456"
        val repeatPass = "123456"
        async(Dispatchers.Main){ signUpViewModel.signUp(name, email, password, repeatPass) }.await()

        val actual = stateCommunication.state.emailErrorMessageRes
        val expected = 1
        assertEquals(expected, actual)

    }


    @Test
    fun `testing signUp method with firebaseAuthInvalidCredentials error`() = runBlocking{
        val repository = RepositoryTest(isSuccess = false,firebaseAuthInvalidCredentials)
        val successCommunicationTest = SignUpSuccessCommunicationTest()
        val stateCommunication = SignUpStateCommunicationTest()
        val signUpViewModel = SignUpViewModel(repository,successCommunicationTest,stateCommunication)
        val email = "email@gmail.com"
        val name = "name"
        val password = "123456"
        val repeatPass = "123456"
        async(Dispatchers.Main){ signUpViewModel.signUp(name, email, password, repeatPass) }.await()

        val actual = stateCommunication.state.emailErrorMessageRes
        val expected = 4
        assertEquals(expected, actual)

    }



    class SignUpSuccessCommunicationTest : SignUpSuccessCommunication {

        var liveData = false

        override fun map(result: Boolean) {
            liveData = result
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<Boolean>) {

        }
    }

    class SignUpStateCommunicationTest : SignUpStateCommunication {

        var state = State()


        override fun map(e: Exception) {
            when(e){
                is PasswordMismatchException -> handlePasswordMismatch()
                is EmptyFieldException ->  handleEmptyFieldError(e)
                is NotEnoughCharsException -> handleNotEnoughCharsException()
                is LoginViewModelTest.FirebaseAuthInvalidCredentials -> {
                    handleInvalidEmailException()
                    hideProgress()
                }
                is LoginViewModelTest.FirebaseAuthUserCollision -> handleAccountAlreadyExist()
            }
        }

        override fun observe(
            owner: LifecycleOwner,
            observer: Observer<SignUpStateCommunication.State>
        ) {
           //do not use here
        }



        private fun handlePasswordMismatch() {
            state =
                state.copy(repeatPasswordErrorMessageRes = -1)
            hideProgress()
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
                Field.NAME -> state.copy(usernameErrorMessageRes = 6)
                Field.PASSWORD -> state.copy(passwordErrorMessageRes = 6)
            }
            hideProgress()
        }

        override fun showProgress() {
            state = State(signUpInProgress = true)
        }

        override fun hideProgress() {
            state = state.copy(signUpInProgress = false)
        }


        data class State(
            @StringRes val emailErrorMessageRes: Int = 0,
            @StringRes val passwordErrorMessageRes: Int = 0,
            @StringRes val repeatPasswordErrorMessageRes: Int = 0,
            @StringRes val usernameErrorMessageRes: Int = 0,
             val signUpInProgress: Boolean = false,
        ) {
            val showProgress: Boolean get() = signUpInProgress
            val enableViews: Boolean get() = !signUpInProgress
        }


    }

    class RepositoryTest(val isSuccess: Boolean = true, val exception: Exception) :
        LoginRepository {

        var isSignIn = false

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
            if (isSuccess) {
                callback.success()
                isSignIn = true
            }
            else callback.map(exception)
        }

        override suspend fun signOut() {
            isSignIn = false
        }
    }

}