package com.cbu.mobil_dersi_projesi.data.model

data class Weather(
    val data: String? = "",
    val day: String? = "",
    val icon: String? = "",
    val description: String? = "",
    val status: String? = "",
    val degree: Double?,
    val min: Double?,
    val max: Double?,
    val night: Double?,
    val humidity: Double?
)