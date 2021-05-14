package com.heronet.weatherify.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    companion object {
        val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        val APP_ID = "5d9d5038ee163d9feea89917d97be020"
    }
    @GET("weather") // Current Weather
    suspend fun getCurrentInfo(
        @Query("q") city: String,
        @Query("units") unit: String,
        @Query("APPID") appId: String = APP_ID
    ): Response<Weather>

    @GET("weather") // Current Weather using COord
    suspend fun getCurrentInfo(
        @Query("lat") lat: Number,
        @Query("lon") lon: Number,
        @Query("units") unit: String = "metric",
        @Query("APPID") appId: String = APP_ID
    ): Response<Weather>

    @GET("onecall")
    suspend fun getForecast(
        @Query("lat") lat: Number,
        @Query("lon") lon: Number,
        @Query("units") unit: String = "metric",
        @Query("exclude") exclude: String = "minutely,hourly",
        @Query("APPID") appId: String = APP_ID
    ): Forecast
}