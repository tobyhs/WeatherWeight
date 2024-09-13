package io.github.tobyhs.weatherweight.data

import io.github.tobyhs.weatherweight.api.accuweather.AccuWeatherCoroutinesService
import io.github.tobyhs.weatherweight.api.accuweather.forecasts.DailyForecast
import io.github.tobyhs.weatherweight.api.accuweather.forecasts.DailyForecastResponse
import io.github.tobyhs.weatherweight.api.accuweather.forecasts.DayPeriod
import io.github.tobyhs.weatherweight.api.accuweather.forecasts.Temperature
import io.github.tobyhs.weatherweight.api.accuweather.forecasts.TemperatureRange
import io.github.tobyhs.weatherweight.api.accuweather.locations.AdministrativeArea
import io.github.tobyhs.weatherweight.api.accuweather.locations.City
import io.github.tobyhs.weatherweight.api.accuweather.locations.Country
import io.github.tobyhs.weatherweight.data.model.HourlyForecast

import io.mockk.coEvery
import io.mockk.mockk

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test

import java.time.Clock
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class AccuWeatherCoroutinesRepositoryTest {
    private val accuWeatherService = mockk<AccuWeatherCoroutinesService>()
    private val clock = Clock.systemUTC()
    private val testDispatcher = StandardTestDispatcher()
    private val repository = AccuWeatherCoroutinesRepository(accuWeatherService, clock, testDispatcher)

    @Test
    fun `getForecast when location is not found`() = runTest(testDispatcher) {
        val location = "Nowhere, USA"
        coEvery { accuWeatherService.searchCities(location, 0) } returns emptyList()
        var error: LocationNotFoundError? = null
        try {
            repository.getForecast(location)
        } catch (e: LocationNotFoundError) {
            error = e
        }
        assertThat(error, notNullValue())
        assertThat(error?.message, equalTo("Location $location not found"))
    }

    @Test
    fun getForecast() = runTest(testDispatcher) {
        val locationKey = "123456"
        val cities: List<City> = listOf(
            City(
                key = locationKey,
                localizedName = "San Jose",
                administrativeArea = AdministrativeArea(id = "CA"),
                country = Country(id = "US"),
            ),
            City(
                key = "432165",
                localizedName = "San Jose",
                administrativeArea = AdministrativeArea(id = "SJ"),
                country = Country(id = "CR"),
            ),
        )
        coEvery { accuWeatherService.searchCities("San Jose", 0) } returns cities

        val time = ZonedDateTime.parse("2018-01-29T12:30:00Z")
        val dailyForecasts: List<DailyForecast> = listOf(
            DailyForecast(
                date = time,
                temperature = TemperatureRange(
                    minimum = Temperature(value = 50.0),
                    maximum = Temperature(value = 60.0),
                ),
                day = DayPeriod(iconPhrase = "Sunny", precipitationProbability = 0),
                night = DayPeriod(iconPhrase = "Sunny", precipitationProbability = 1),
            ),
            DailyForecast(
                date = time.plusDays(1),
                temperature = TemperatureRange(
                    minimum = Temperature(value = 52.0),
                    maximum = Temperature(value = 62.0),
                ),
                day = DayPeriod(iconPhrase = "Cloudy", precipitationProbability = 70),
                night = DayPeriod(iconPhrase = "Showers", precipitationProbability = 50),
            ),
        )
        val dailyForecastResponse = DailyForecastResponse(dailyForecasts = dailyForecasts)
        coEvery { accuWeatherService.get5DayForecast(locationKey) } returns dailyForecastResponse

        val hourlyTime = ZonedDateTime.parse("2018-01-29T13:00:00Z")
        coEvery { accuWeatherService.get12HourForecast(locationKey) } returns listOf(
            io.github.tobyhs.weatherweight.api.accuweather.forecasts.HourlyForecast(
                dateTime = hourlyTime,
                iconPhrase = "Mostly sunny",
                temperature = Temperature(value = 55.0),
                precipitationProbability = 1,
            )
        )
        val result = repository.getForecast("San Jose")

        assertThat(result.location, equalTo("San Jose, CA, US"))
        assertThat(
            result.publicationTime.until(ZonedDateTime.now(clock), ChronoUnit.MINUTES),
            equalTo(0L)
        )
        assertThat(result.dailyForecasts.size, equalTo(2))

        var dailyForecast = result.dailyForecasts[0]
        assertThat(dailyForecast.date.toString(), equalTo("2018-01-29"))
        assertThat(dailyForecast.low, equalTo(50))
        assertThat(dailyForecast.high, equalTo(60))
        assertThat(dailyForecast.text, equalTo("Sunny"))
        assertThat(dailyForecast.precipitationProbability, equalTo(1))

        dailyForecast = result.dailyForecasts[1]
        assertThat(dailyForecast.date.toString(), equalTo("2018-01-30"))
        assertThat(dailyForecast.low, equalTo(52))
        assertThat(dailyForecast.high, equalTo(62))
        assertThat(dailyForecast.text, equalTo("Cloudy / Showers"))
        assertThat(dailyForecast.precipitationProbability, equalTo(70))

        assertThat(result.hourlyForecasts, equalTo(listOf(
            HourlyForecast(
                dateTime = hourlyTime,
                text = "Mostly sunny",
                temperature = 55,
                precipitationProbability = 1,
            )
        )))
    }
}
