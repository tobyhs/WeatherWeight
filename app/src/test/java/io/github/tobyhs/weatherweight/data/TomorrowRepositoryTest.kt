package io.github.tobyhs.weatherweight.data

import io.github.tobyhs.weatherweight.api.tomorrow.TomorrowService
import io.github.tobyhs.weatherweight.api.tomorrow.forecast.DailyForecast
import io.github.tobyhs.weatherweight.api.tomorrow.forecast.DailyForecastValues
import io.github.tobyhs.weatherweight.api.tomorrow.forecast.ForecastResponse
import io.github.tobyhs.weatherweight.api.tomorrow.forecast.ForecastTimelines
import io.github.tobyhs.weatherweight.api.tomorrow.forecast.HourlyForecast
import io.github.tobyhs.weatherweight.api.tomorrow.forecast.HourlyForecastValues
import io.github.tobyhs.weatherweight.api.tomorrow.forecast.Location

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test

import java.time.Clock
import java.time.ZoneOffset
import java.time.ZonedDateTime

class TomorrowRepositoryTest {
    private val tomorrowService = mockk<TomorrowService>()
    private val clock = mockk<Clock>()
    private val testDispatcher = StandardTestDispatcher()
    private val repository = TomorrowRepository(tomorrowService, clock, testDispatcher)

    @Test
    fun getForecast() = runTest(testDispatcher) {
        val time = ZonedDateTime.parse("2025-09-08T12:00:00Z")
        every { clock.instant() } returns time.toInstant()
        every { clock.zone } returns ZoneOffset.UTC
        val forecastResponse = ForecastResponse(
            timelines = ForecastTimelines(
                daily = listOf(
                    DailyForecast(
                        time = time,
                        values = DailyForecastValues(
                            temperatureMin = 65.8,
                            temperatureMax = 70.2,
                            precipitationProbabilityMax = 5,
                            weatherCodeMin = 1100,
                            weatherCodeMax = 1101,
                        )
                    ),
                    DailyForecast(
                        time = time.plusDays(1),
                        values = DailyForecastValues(
                            temperatureMin = 66.0,
                            temperatureMax = 72.0,
                            precipitationProbabilityMax = 2,
                            weatherCodeMin = 2000,
                            weatherCodeMax = 2000,
                        )
                    )
                ),
                hourly = listOf(
                    HourlyForecast(
                        time = time,
                        values = HourlyForecastValues(
                            temperature = 67.0,
                            precipitationProbability = 2,
                            weatherCode = 2000,
                        )
                    )
                )
            ),
            location = Location(name = "San Francisco, California, United States")
        )
        val location = "San Francisco"
        coEvery { tomorrowService.getForecast(location) } returns forecastResponse

        val result = repository.getForecast(location)
        assertThat(result.location, equalTo("San Francisco, California, United States"))
        assertThat(result.publicationTime, equalTo(time))

        var dailyForecast = result.dailyForecasts[0]
        assertThat(dailyForecast.date.toString(), equalTo("2025-09-08"))
        assertThat(dailyForecast.low, equalTo(66))
        assertThat(dailyForecast.high, equalTo(70))
        assertThat(dailyForecast.precipitationProbability, equalTo(5))
        assertThat(dailyForecast.text, equalTo("Mostly Clear / Partly Cloudy"))
        dailyForecast = result.dailyForecasts[1]
        assertThat(dailyForecast.text, equalTo("Fog"))

        assertThat(result.hourlyForecasts[0], equalTo(io.github.tobyhs.weatherweight.data.model.HourlyForecast(
            dateTime = time,
            text = "Fog",
            temperature = 67,
            precipitationProbability = 2,
        )))
    }
}
