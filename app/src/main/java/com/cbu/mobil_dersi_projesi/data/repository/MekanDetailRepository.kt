package com.cbu.mobil_dersi_projesi.data.repository

import com.cbu.mobil_dersi_projesi.common.BaseRepository
import com.cbu.mobil_dersi_projesi.data.model.WeatherResponse
import com.cbu.mobil_dersi_projesi.network.Api
import com.cbu.mobil_dersi_projesi.util.Resource

class MekanDetailRepository(private val api: Api) : BaseRepository() {

    suspend fun getWeather(lang: String, city: String): Resource<WeatherResponse> {
        return getResult {
            api.getWeather(lang, city)
        }
    }
}