package com.cbu.mobil_dersi_projesi.data.model

data class WeatherResponse(
    val city: String? = "",
    val result: List<Weather>? = null
)