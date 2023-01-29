package com.android.loginapp.maps

import com.android.loginapp.maps.model.PreferencesManager
import org.junit.Assert.*
import org.junit.Test

class MainViewModelTest{

    @Test
    fun `testing saving location`(){
        val sharedPreferenceManager = SharedPreferenceManagerTest()
        val mainViewModel = MainViewModel(sharedPreferenceManager)

        val actualBeforeUsing = sharedPreferenceManager.myLocation
        val expectedBeforeUsing = Pair(0F,0F)
        assertEquals(expectedBeforeUsing,actualBeforeUsing)
        mainViewModel.saveLocation(33.1111,55.12121)
        val actual = sharedPreferenceManager.myLocation
        val expected = Pair(33.1111F,55.12121F)
        assertEquals(expected, actual)
    }



    class SharedPreferenceManagerTest: PreferencesManager{

        var myLocation =  Pair(0F,0F)

        override fun saveLocation(lat: Double, lon: Double) {
            myLocation = Pair(lat.toFloat(),lon.toFloat())
        }

        override fun getLocation(): Pair<Float, Float> {
            TODO("Not yet implemented")
        }
    }
}