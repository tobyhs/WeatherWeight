package io.github.tobyhs.weatherweight.api.tomorrow.forecast

import io.github.tobyhs.weatherweight.di.AppModule

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test

import java.io.InputStreamReader
import java.time.format.DateTimeFormatter

class ForecastResponseTest {
    @Test
    fun parseJson() {
        javaClass.getResourceAsStream("/tomorrow/forecast.json").use { stream ->
            InputStreamReader(stream).use { reader ->
                val moshi = AppModule().provideMoshi()
                val moshiAdapter = moshi.adapter(ForecastResponse::class.java)
                val forecastResponse = moshiAdapter.fromJson(reader.readText())!!

                val location = forecastResponse.location
                assertThat(location.name, equalTo("San Francisco, California, United States"))

                val hourlyForecast = forecastResponse.timelines.hourly[60]
                assertThat(hourlyForecast.time.toString(), equalTo("2025-09-08T12:00Z"))
                assertThat(hourlyForecast.values, equalTo(HourlyForecastValues(
                    temperature = 58.2,
                    precipitationProbability = 5,
                    weatherCode = 2000,
                )))

                val dailyForecast = forecastResponse.timelines.daily[2]
                assertThat(dailyForecast.time.format(DateTimeFormatter.ISO_DATE), equalTo("2025-09-07Z"))
                assertThat(dailyForecast.values, equalTo(DailyForecastValues(
                    temperatureMin = 57.5,
                    temperatureMax = 68.8,
                    precipitationProbabilityMax = 5,
                    weatherCodeMin = 2000,
                    weatherCodeMax = 2000,
                )))
            }
        }
    }
}
