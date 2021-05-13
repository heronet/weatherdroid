package com.heronet.weatherify.data

import com.heronet.weatherify.api.WeatherAPI
import com.heronet.weatherify.util.networkBoundResource
import javax.inject.Inject

class Repository @Inject constructor(private val api: WeatherAPI) {
    suspend fun getCurrentInfo(location: String, unit: String) = api.getCurrentInfo(location, unit)
    fun getForecast(lat: Number, lon: Number) = networkBoundResource(
        fetch = {
            api.getForecast(lat, lon)
        }
    )
}