package com.example.nhtu.demochat.Api

import com.example.nhtu.demochat.Model.GetSessionInput
import com.example.nhtu.demochat.Model.GetSessionOutput
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface IApi {
    @Headers("Content-Type: application/json")
    @POST("/partner/authenticate")
    fun getSession(@Body input: GetSessionInput): Call<GetSessionOutput>
}