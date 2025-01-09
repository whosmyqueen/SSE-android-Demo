package com.whosmyqueen.ssedemo

import kotlinx.coroutines.flow.Flow
import retrofit2.Event
import retrofit2.ServerSendEvents
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Classname: Api
 * Description: TODO
 * Created by Leobert on 2023/11/28.
 */
interface Api {
    @ServerSendEvents
    @GET("http://10.4.148.63:8080/test")
    fun word(@Query("string") string: String): Flow<Event>

    @GET("http://192.168.8.112:8080/test/words")
    suspend fun test(): Obj
}