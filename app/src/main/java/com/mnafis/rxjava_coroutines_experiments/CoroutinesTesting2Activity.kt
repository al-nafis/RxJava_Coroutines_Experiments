package com.mnafis.rxjava_coroutines_experiments

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mnafis.rxjava_coroutines_experiments.databinding.ActivityCoroutinesTesting2Binding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.CoroutineContext

class CoroutinesTesting2Activity : AppCompatActivity(), CoroutineScope {

    private val TAG = "TESTING:"

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + errorHandler

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        println("$TAG ${throwable.localizedMessage}")
    }

    private val job = Job()

    private val service = MockApiService()

    private val viewModel: CoroutinesTesting2ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityCoroutinesTesting2Binding =
            DataBindingUtil.setContentView(this, R.layout.activity_coroutines_testing_2)
        binding.viewModel = viewModel
        binding.activity = this
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    fun streamUsers(shouldPass: Boolean) {
        println("$TAG ---------------------------------------------")
        launch {
            service.streamUsers(shouldPass)
                .collect { user -> println("$TAG $user") }
        }
        println("$TAG User Names")
    }
}