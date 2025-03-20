package com.example.weather_app.Models

import com.google.gson.annotations.SerializedName


data class ForecastDay(

    @SerializedName("temperature") var temperature: Double,
    @SerializedName("temperatureApparent") var temperatureApparent: Double,
    @SerializedName("temperatureAvg") var tempAvg: Double,
    @SerializedName("temperatureMax") var tempMax: Double,
    @SerializedName("temperatureMin") var tempMin: Double,

    @SerializedName("humidityAvg") var humidityAvg: Double,
    @SerializedName("humidityMax") var humidityMax: Double,
    @SerializedName("humidityMin") var humidityMin: Double,
    @SerializedName("humidity") var humidity: Double,


    @SerializedName("windSpeed") var windSpeed: Double,
    @SerializedName("windSpeedAvg") var windSpeedAvg: Double,
    @SerializedName("windSpeedMax") var windSpeedMax: Double,
    @SerializedName("windSpeedMin") var windSpeedMin: Double,
    @SerializedName("windDirection") var windDirection: Double,
    @SerializedName("windGust") var windGust: Double,

    @SerializedName("pressureSeaLevelAvg") var pressureSeaLevelAvg: Double,
    @SerializedName("pressureSeaLevelMax") var pressureSeaLevelMax: Double,
    @SerializedName("pressureSeaLevelMin") var pressureSeaLevelMin: Double,

    @SerializedName("uvIndex") var uvIndex: Double,
    @SerializedName("uvIndexMax") var uvIndexMax: Double,
    @SerializedName("uvHealthConcern") var uvHealthConcern: Double,

    @SerializedName("precipitationProbabilityAvg") var precipitationProbabilityAvg: Double,

    @SerializedName("sunriseTime") var sunriseTime: String,
    @SerializedName("sunsetTime") var sunsetTime: String,
    @SerializedName("moonriseTime") var moonriseTime: String,
    @SerializedName("moonsetTime") var moonsetTime: String,

    @SerializedName("weatherCode") var weatherCode: Double,
    @SerializedName("weatherCodeMax") var weatherCodeMax: Double,
    @SerializedName("weatherCodeMin") var weatherCodeMin: Double,

    @SerializedName("cloudBase") var cloudBase: Double,
    @SerializedName("cloudCeiling") var cloudCeiling: Double,
    @SerializedName("cloudCover") var cloudCover: Int,

    @SerializedName("dewPoint") var dewPoDouble
    : Double,
    @SerializedName("evapotranspiration") var evapotranspiration: Double,
    @SerializedName("freezingRainIntensity") var freezingRainIntensity: Double,

    @SerializedName("hailProbability") var hailProbability: Double,
    @SerializedName("hailSize") var hailSize: Double,

    @SerializedName("iceAccumulation") var iceAccumulation: Double,
    @SerializedName("iceAccumulationLwe") var iceAccumulationLwe: Double,

    @SerializedName("rainAccumulation") var rainAccumulation: Double,
    @SerializedName("rainAccumulationLwe") var rainAccumulationLwe: Double,
    @SerializedName("rainIntensity") var rainIntensity: Double,

    @SerializedName("sleetAccumulation") var sleetAccumulation: Double,
    @SerializedName("sleetAccumulationLwe") var sleetAccumulationLwe: Double,
    @SerializedName("sleetIntensity") var sleetIntensity: Double,

    @SerializedName("snowAccumulation") var snowAccumulation: Double,
    @SerializedName("snowAccumulationLwe") var snowAccumulationLwe: Double,
    @SerializedName("snowDepth") var snowDepth: Double,
    @SerializedName("snowIntensity") var snowIntensity: Double,

    @SerializedName("visibility") var visibility: Double
)