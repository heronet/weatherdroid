package com.heronet.weatherify.data


import com.heronet.weatherify.api.WeatherAPI
import com.heronet.weatherify.util.networkBoundResource
import javax.inject.Inject

class Repository @Inject constructor(private val api: WeatherAPI) {
    suspend fun getCurrentInfo(location: String, unit: String) = api.getCurrentInfo(location, unit) // Has nothing to do with getForcast
    fun getForecast(lat: Number, lon: Number) = networkBoundResource(
        fetch = {
            val cur = api.getCurrentInfo(lat, lon)
            val fore = api.getForecast(lat, lon)
            fore.cityName = cur.body()?.name
            fore
        }
    )
}