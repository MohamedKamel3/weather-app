package com.example.weather_app.Models

data class ApiErrorResponse(
    val code: Int,
    val type: String,
    val message: String
)