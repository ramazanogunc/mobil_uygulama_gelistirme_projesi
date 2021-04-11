package com.cbu.mobil_dersi_projesi.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherClient {

    private val retrofit = Retrofit
        .Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.collectapi.com/")
        .build()

    fun getService(): Api = retrofit.create(Api::class.java)
}