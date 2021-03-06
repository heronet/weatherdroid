package com.heronet.weatherify.api


import com.google.gson.annotations.SerializedName

data class Weather(
    val base: String,
    val clouds: Clouds, // TODO: 5/12/21  
    val cod: Int,
    val coord: Coord, // TODO: 5/12/21  
    val dt: Int,
    val id: Int,
    val main: Main, // TODO: 5/12/21  
    val name: String,
    val sys: Sys,// TODO: 5/12/21  
    val timezone: Int,
    val visibility: Int,
    val weather: List<WeatherX>,// TODO: 5/12/21  
    val wind: Wind // TODO: 5/12/21  
) {
    data class Clouds(
        val all: Int
    )

    data class Coord(
        val lat: Double,
        val lon: Double
    )

    data class Main(
        @SerializedName("feels_like")
        val feelsLike: Double,
        @SerializedName("grnd_level")
        val grndLevel: Int,
        val humidity: Int,
        val pressure: Int,
        @SerializedName("sea_level")
        val seaLevel: Int,
        val temp: Double,
        @SerializedName("temp_max")
        val tempMax: Double,
        @SerializedName("temp_min")
        val tempMin: Double
    )

    data class Sys(
        val country: String,
        val sunrise: Int,
        val sunset: Int
    )

    data class WeatherX(
        val description: String,
        val icon: String,
        val id: Int,
        val main: String
    )

    data class Wind(
        val deg: Int,
        val gust: Double,
        val speed: Double
    )
}