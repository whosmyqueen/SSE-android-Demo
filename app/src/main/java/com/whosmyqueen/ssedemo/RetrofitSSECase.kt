package com.whosmyqueen.ssedemo

import okhttp3.OkHttpClient
import okhttp3.sse.EventSources
import retrofit2.EventSourceAdapter
import retrofit2.Retrofit
import retrofit2.RetrofitSSE
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.sse.EventSourcesExt

/**
 * Classname: RetrofitSSECase </p>
 * Description: TODO </p>
 * Created by Leobert on 2023/11/28.
 */
object RetrofitSSECase {
    val sse = OkHttpClient().let { okhttp3 ->
        RetrofitSSE(
            Retrofit.Builder()
                .baseUrl("http://192.168.8.112:8080")
                .client(okhttp3)
                .addConverterFactory(GsonConverterFactory.create())
                .build(),
            EventSourcesExt.createFactory(okhttp3)
        ).addEventSourceAdapterFactory(EventSourceAdapter.FlowAdapter.Companion.Factory)
    }

    val api = sse.create(Api::class.java)
}