package com.android.loginapp.maps.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.android.loginapp.login.model.*
import com.android.loginapp.login.presentation.AuthCallback
import com.android.loginapp.maps.model.PreferencesManager
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
import java.net.UnknownHostException

class MapsViewModelTest{

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

    @Test
    fun `testing getUsername method`() = runBlocking{
        val sharedPreferenceManagerTest = SharedPreferenceManagerTest()
        val repository = RepositoryTest()
        val currentNameCommunication = CurrentNameCommunicationTest()
        val locationCommunication = LocationCommunicationTest()
        val viewModel = MapsViewModel(sharedPreferenceManagerTest,repository,currentNameCommunication,locationCommunication)

        async(Dispatchers.Main) {
            viewModel.getUsername()
        }.await()

        val actual = currentNameCommunication.currentName
        val expected = "name"

        assertEquals(expected, actual)
    }


    @Test
    fun `testing signOut method`() = runBlocking{
        val sharedPreferenceManagerTest = SharedPreferenceManagerTest()
        val repository = RepositoryTest()
        val currentNameCommunication = CurrentNameCommunicationTest()
        val locationCommunication = LocationCommunicationTest()
        val viewModel = MapsViewModel(sharedPreferenceManagerTest,repository,currentNameCommunication,locationCommunication)

        async(Dispatchers.Main) {
            viewModel.signOut()
        }.await()

        val actual = repository.isSignIn
        val expected = false

        assertEquals(expected, actual)
    }


    @Test
    fun `testing changeName method`() = runBlocking{
        val sharedPreferenceManagerTest = SharedPreferenceManagerTest()
        val repository = RepositoryTest()
        val currentNameCommunication = CurrentNameCommunicationTest()
        val locationCommunication = LocationCommunicationTest()
        val viewModel = MapsViewModel(sharedPreferenceManagerTest,repository,currentNameCommunication,locationCommunication)

        async(Dispatchers.Main) {
            viewModel.changeName("newName")
        }.await()

        val actual = currentNameCommunication.currentName
        val expected = "newName"

        assertEquals(expected, actual)
    }


    @Test
    fun `testing getLocation method`() = runBlocking{
        val sharedPreferenceManagerTest = SharedPreferenceManagerTest()
        val repository = RepositoryTest()
        val currentNameCommunication = CurrentNameCommunicationTest()
        val locationCommunication = LocationCommunicationTest()
        val viewModel = MapsViewModel(sharedPreferenceManagerTest,repository,currentNameCommunication,locationCommunication)

        sharedPreferenceManagerTest.saveLocation(33.0,30.0)
        async(Dispatchers.Main) {
            viewModel.getLocation()
        }.await()

        val actual = locationCommunication.location
        val expected =  Pair(33.0,30.0)

        assertEquals(expected, actual)
    }

    class SharedPreferenceManagerTest: PreferencesManager {

        var myLocation =  Pair(0F,0F)

        override fun saveLocation(lat: Double, lon: Double) {
            myLocation = Pair(lat.toFloat(),lon.toFloat())
        }

        override fun getLocation(): Pair<Float, Float> {
            return myLocation
        }
    }

    class RepositoryTest(val isSuccess: Boolean = true, val exception: Exception = UnknownHostException()) :
        LoginRepository {

        var currentName = "name"

        var isSignIn = true

        override suspend fun changeName(name: String, callback: SuccessRequestCallback) {
            currentName = name
            callback.success()
        }

        override suspend fun getName(): String {
            return currentName
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
            isSignIn = false
        }
    }

    class CurrentNameCommunicationTest: CurrentNameCommunication{

        var currentName = ""

        override fun map(name: String) {
            currentName = name
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<String>) {
            //do not use here
        }
    }

    class LocationCommunicationTest: LocationCommunication{

        var location = Pair(0.0,0.0)

        override fun map(location: Pair<Double, Double>) {
            this.location = location
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<Pair<Double, Double>>) {
            //do not use here
        }
    }

}