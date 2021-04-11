package com.cbu.mobil_dersi_projesi.network

import com.cbu.mobil_dersi_projesi.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface Api {
    @Headers(
        "authorization", "apikey 401qOj8Z7Bi8vI1BUTclWX:7n1shCFwU5mjON4nzR4Xk4"
    )
    @GET("weather/getWeather")
    suspend fun getWeather(
        @Query("data.lang") lang: String,
        @Query("data.city") city: String
    ): Response<WeatherResponse>
}