package com.dis.reactiverestapi.service

import com.dis.reactiverestapi.model.DailyForecast
import com.dis.reactiverestapi.model.ForecastResponse
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import reactor.util.retry.Retry
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class WeatherService(private val webClient: WebClient, @Value("\${weather.api.url}") private val weatherApiUrl: String) {
    fun getCurrentDayForecast(): Mono<ForecastResponse> {
        return webClient.get()
                .uri("$weatherApiUrl/gridpoints/MLB/33,70/forecast")
                .retrieve()
                .onStatus({ statusCode -> statusCode == HttpStatus.INTERNAL_SERVER_ERROR },
                    { response ->
                        response.bodyToMono(String::class.java)
                            .flatMap { body ->
                                Mono.error(IllegalStateException("Internal Server Error: $body"))
                            }
                    }
                )
                .bodyToMono(JsonNode::class.java)
                .flatMap { jsonNode ->
                    val forecasts = jsonNode.path("properties").path("periods")
                    val today = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH))

                    val todayForecast = forecasts.first()

                    if (todayForecast != null) {
                        val highTemp = todayForecast.path("temperature").asDouble()
                        val forecastBlurb = todayForecast.path("shortForecast").asText()

                        Mono.just(ForecastResponse(listOf(DailyForecast(
                                day_name = today,
                                temp_high_celsius = fahrenheitToCelsius(highTemp),
                                forecast_blurp = forecastBlurb
                        ))))
                    } else {
                        Mono.error(IllegalStateException("Could not find forecast for today"))
                    }
                }.retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2))
                .filter { throwable -> throwable is WebClientResponseException.InternalServerError })
                .onErrorResume { e ->
                    Mono.just(ForecastResponse(emptyList()))
                }
    }

    private fun fahrenheitToCelsius(fahrenheit: Double): Double {
        return (fahrenheit - 32) * 5 / 9
    }
}