package com.example.imgurapp.network


import com.example.imgurapp.dataClass.data
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header


interface MyApi {

    @GET("1")
    fun getList(@Header("Authorization") authorization: String )
    : Call<data>



}