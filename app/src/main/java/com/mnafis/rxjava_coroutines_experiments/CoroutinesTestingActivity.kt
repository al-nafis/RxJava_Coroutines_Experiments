package com.mnafis.rxjava_coroutines_experiments

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mnafis.rxjava_coroutines_experiments.databinding.ActivityCoroutinesTestingBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.CoroutineContext

class CoroutinesTestingActivity() : AppCompatActivity(), CoroutineScope {

    private val TAG = "TESTING:"

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val job = Job()

    private val disposable = CompositeDisposable()

    private val service = MockApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityCoroutinesTestingBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_coroutines_testing)
        binding.activity = this
    }

    override fun onDestroy() {
        disposable.dispose()
        job.cancel()
        super.onDestroy()
    }

    fun rxKotlinBasic(shouldPass: Boolean) {
        println("$TAG ---------------------------------------------")
        disposable.add(
            service.getUserListRx(shouldPass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> println("$TAG $result") },
                    { error -> println("$TAG ${error.localizedMessage}") }
                )
        )
        println("$TAG User names")
    }

    fun coroutinesBasic(shouldPass: Boolean) {
        println("$TAG ---------------------------------------------")
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val list: List<String> = service.getUserListCoroutines(shouldPass)
                println("$TAG $list")
            } catch (e: Exception) {
                println("$TAG ${e.localizedMessage}")
            }
        }
        println("$TAG User names")
    }

    /////////////////////////////////////////////////////////////

    fun rxKotlinDataStreamTest(shouldPass: Boolean) {
        println("$TAG ---------------------------------------------")
        disposable.add(
            service.getUserOneAtATimeRx(shouldPass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { user -> println("$TAG $user") },
                    { error -> println("$TAG Error: ${error.localizedMessage}") }
                )
        )
        println("$TAG User Names")
    }

    fun coroutinesDataStreamTest(shouldPass: Boolean) {
        println("$TAG ---------------------------------------------")
        launch(Dispatchers.IO) {
            service.getUserOneAtATimeCoroutines(shouldPass)
                .catch { exception -> println("$TAG Error: ${exception.localizedMessage}") }
                .collect { user -> println("$TAG $user") }
        }
        println("$TAG User Names")
    }

    fun nextPage() = startActivity(Intent(this, CoroutinesTesting2Activity::class.java))
}