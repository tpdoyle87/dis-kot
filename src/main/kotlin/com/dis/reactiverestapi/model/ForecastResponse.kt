package com.dis.reactiverestapi.model

data class DailyForecast(
        val day_name: String,
        val temp_high_celsius: Double,
        val forecast_blurp: String
)

data class ForecastResponse(
        val daily: List<DailyForecast>
)
