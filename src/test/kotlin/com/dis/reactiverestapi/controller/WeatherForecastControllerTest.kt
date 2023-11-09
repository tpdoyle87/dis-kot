import com.dis.reactiverestapi.ReactiveRestApiApplication
import com.dis.reactiverestapi.controller.WeatherForecastController
import com.dis.reactiverestapi.model.DailyForecast
import com.dis.reactiverestapi.model.ForecastResponse
import com.dis.reactiverestapi.service.WeatherService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Mono

@ExtendWith(SpringExtension::class)
@WebFluxTest(WeatherForecastController::class)
@ContextConfiguration(classes = [ReactiveRestApiApplication::class])
class WeatherForecastControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockBean
    private lateinit var weatherService: WeatherService

    @Test
    fun `should return current day forecast`() {
        // Arrange
        val forecastResponse = ForecastResponse(listOf(DailyForecast("Monday", 27.2, "Partly Sunny")))
        given(weatherService.getCurrentDayForecast()).willReturn(Mono.just(forecastResponse))

        // Act & Assert
        webTestClient.get().uri("/forecast/current")
            .exchange()
            .expectStatus().isOk
            .expectBody<ForecastResponse>()
            .consumeWith { response ->
                assert(response.responseBody?.daily?.get(0)?.day_name == "Monday")
                assert(response.responseBody?.daily?.get(0)?.temp_high_celsius == 27.2)
                assert(response.responseBody?.daily?.get(0)?.forecast_blurp == "Partly Sunny")
            }
    }
}
