package com.mnafis.rxjava_coroutines_experiments

import io.reactivex.Observable
import io.reactivex.Single
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit

class MockApiService {

    private val user1 = "User 1"
    private val user2 = "User 2"
    private val user3 = "User 3"
    private val list = listOf(user1, user2, user3)
    private val errorMessage = "Api Service Failed"

    fun getUserListRx(shouldPass: Boolean): Single<List<String>> =
        if (shouldPass) Single.just(list).delay(2, TimeUnit.SECONDS)
        else Single.error(Throwable(errorMessage))

    fun getUserOneAtATimeRx(shouldPass: Boolean): Observable<String> =
        if (shouldPass) Observable.fromIterable(list)
            .zipWith(Observable.interval(1, TimeUnit.SECONDS)) { user: String, _: Long -> user }
        else Observable.error(Throwable(errorMessage))

    ///////////////////////////////////////////////////////////////////

    suspend fun getUserListCoroutines(shouldPass: Boolean): List<String> {
        delay(2000L)
        return if (shouldPass) list else throw Exception(errorMessage)
    }

    fun getUserOneAtATimeCoroutines(shouldPass: Boolean): Flow<String> =
        flow {
            for (user in list) {
                delay(1000L)
                if (shouldPass) emit(user)
                else throw Exception(errorMessage)
            }
        }

    ///////////////////////////////////////////////////////////////////

    fun streamUsers(shouldPass: Boolean): Flow<String> =
        flow {
            for (user in list) {
                delay(1000L)
                if (!shouldPass && user == "User 3") throw Exception(errorMessage)
                else emit(user)
            }
        }

    ///////////////////////////////////////////////////////////////////

    suspend fun getFirstName(shouldPass: Boolean): String {
        delay(2000L)
        return if (shouldPass) "James" else throw Exception(errorMessage)
    }


    suspend fun getLastName(shouldPass: Boolean): String {
        delay(1000L)
        return if (shouldPass) "Bond" else throw Exception(errorMessage)
    }
}