package com.example.nhtu.demochat.Api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiService {

    var api: IApi? = null

    companion object {
        private val apiService: ApiService? = null
        fun getInstance(): ApiService {
                val apiService = ApiService()

                val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build()

                val retrofit = Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://trackingapis.fpt.vn")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                apiService.api = retrofit.create(IApi::class.java)
                return apiService
            }
    }
}
