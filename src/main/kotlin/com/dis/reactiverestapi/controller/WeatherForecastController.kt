package com.dis.reactiverestapi.controller

import com.dis.reactiverestapi.model.ForecastResponse
import com.dis.reactiverestapi.service.WeatherService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/forecast")
class WeatherForecastController(private val weatherService: WeatherService) {

    @GetMapping("/current", produces = ["application/json"])
    fun getCurrentDayForecast(): Mono<ForecastResponse> {
        return weatherService.getCurrentDayForecast()
    }
}