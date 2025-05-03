package com.example.weather_app.constants

import com.example.weather_app.R


object WeatherConstants {
    val weatherCodes = mapOf(
        0 to "Unknown",
        1000 to "Clear, Sunny",
        1100 to "Mostly Clear",
        1101 to "Partly Cloudy",
        1102 to "Mostly Cloudy",
        1001 to "Cloudy",
        2000 to "Fog",
        2100 to "Light Fog",
        4000 to "Drizzle",
        4001 to "Rain",
        4200 to "Light Rain",
        4201 to "Heavy Rain",
        5000 to "Snow",
        5001 to "Flurries",
        5100 to "Light Snow",
        5101 to "Heavy Snow",
        6000 to "Freezing Drizzle",
        6001 to "Freezing Rain",
        6200 to "Light Freezing Rain",
        6201 to "Heavy Freezing Rain",
        7000 to "Ice Pellets",
        7101 to "Heavy Ice Pellets",
        7102 to "Light Ice Pellets",
        8000 to "Thunderstorm"
    )

    val weatherImageMap = mapOf(
        1000 to Pair(R.drawable.a10000_clear_small2x, R.drawable.a10001_clear_small2x),
        1100 to Pair(
            R.drawable.a11000_mostly_clear_small2x,
            R.drawable.a11001_mostly_clear_small2x
        ),
        1101 to Pair(
            R.drawable.a11010_partly_cloudy_small2x,
            R.drawable.a11011_partly_cloudy_small2x
        ),
        1102 to Pair(
            R.drawable.a11020_mostly_cloudy_small2x,
            R.drawable.a11021_mostly_cloudy_small2x
        ),
        1001 to Pair(R.drawable.a10010_cloudy_small2x, null),
        2000 to Pair(R.drawable.a20000_fog_small2x, null),
        2100 to Pair(R.drawable.a21000_fog_light_small2x, null),
        4000 to Pair(R.drawable.a40000_drizzle_small2x, null),
        4001 to Pair(R.drawable.a40010_rain_small2x, null),
        4200 to Pair(R.drawable.a42000_rain_light_small2x, null),
        4201 to Pair(R.drawable.a42010_rain_heavy_small2x, null),
        5000 to Pair(R.drawable.a50000_snow_small2x, null),
        5001 to Pair(R.drawable.a50010_flurries_small2x, null),
        5100 to Pair(R.drawable.a51000_snow_light_small2x, null),
        5101 to Pair(R.drawable.a51010_snow_heavy_small2x, null),
        6000 to Pair(R.drawable.a60000_freezing_rain_drizzle_small, null),
        6001 to Pair(R.drawable.a60010_freezing_rain_small2x, null),
        6200 to Pair(R.drawable.a62000_freezing_rain_light_small2x, null),
        6201 to Pair(R.drawable.a62010_freezing_rain_heavy_small2x, null),
        7000 to Pair(R.drawable.a70000_ice_pellets_small2x, null),
        7101 to Pair(R.drawable.a71010_ice_pellets_heavy_small2x, null),
        7102 to Pair(R.drawable.a71020_ice_pellets_light_small2x, null),
        8000 to Pair(R.drawable.a80000_tstorm_small2x, null)
    )

    val weatherBigImageMap = mapOf(
        1000 to Pair(R.drawable.b10000_clear_large2x, R.drawable.b10001_clear_large2x),
        1100 to Pair(
            R.drawable.b11000_mostly_clear_large2x,
            R.drawable.b11001_mostly_clear_large2x
        ),
        1101 to Pair(
            R.drawable.b11010_partly_cloudy_large2x,
            R.drawable.b11011_partly_cloudy_large2x
        ),
        1102 to Pair(
            R.drawable.b11020_mostly_cloudy_large2x,
            R.drawable.b11021_mostly_cloudy_large2x
        ),
        1001 to Pair(R.drawable.b10010_cloudy_large2x, null),
        2000 to Pair(R.drawable.b20000_fog_large2x, null),
        2100 to Pair(R.drawable.b21000_fog_light_large2x, null),
        4000 to Pair(R.drawable.ab40000_drizzle_large2x, null),
        4001 to Pair(R.drawable.b40010_rain_large2x, null),
        4200 to Pair(R.drawable.b42000_rain_light_large2x, null),
        4201 to Pair(R.drawable.b42010_rain_heavy_large2x, null),
        5000 to Pair(R.drawable.b50000_snow_large2x, null),
        5001 to Pair(R.drawable.b50010_flurries_large2x, null),
        5100 to Pair(R.drawable.b51000_snow_light_large2x, null),
        5101 to Pair(R.drawable.b51010_snow_heavy_large2x, null),
        6000 to Pair(R.drawable.b60000_freezing_rain_drizzle_large2x, null),
        6001 to Pair(R.drawable.b60010_freezing_rain_large2x, null),
        6200 to Pair(R.drawable.b62000_freezing_rain_light_large2x, null),
        6201 to Pair(R.drawable.b62010_freezing_rain_heavy_large2x, null),
        7000 to Pair(R.drawable.b70000_ice_pellets_large2x, null),
        7101 to Pair(R.drawable.b71010_ice_pellets_heavy_large2x, null),
        7102 to Pair(R.drawable.b71020_ice_pellets_light_large2x, null),
        8000 to Pair(R.drawable.b80000_tstorm_large2x, null)
    )
}