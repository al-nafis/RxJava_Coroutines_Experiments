package com.mnafis.rxjava_coroutines_experiments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CoroutinesTesting2ViewModel : ViewModel() {

    private val TAG = "TESTING:"

    private val service = MockApiService()

    fun tellMeYourName(shouldPassFistName: Boolean, shouldPassLastName: Boolean) {
        println("$TAG ---------------------------------------------")
        viewModelScope.launch {
            try {
                val firstName = withContext(Dispatchers.IO) { service.getFirstName(shouldPassFistName) }
                val lastName = withContext(Dispatchers.IO) { service.getLastName(shouldPassLastName) }
                println("$TAG $lastName, $firstName $lastName")
            } catch (e: Exception) {
                println("$TAG ${e.localizedMessage}")
            }
        }
        println("$TAG Tell me your name")
    }
}