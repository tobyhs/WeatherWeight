package io.github.tobyhs.weatherweight.data

import io.github.tobyhs.weatherweight.api.accuweather.AccuWeatherService
import io.github.tobyhs.weatherweight.api.accuweather.forecasts.DailyForecast
import io.github.tobyhs.weatherweight.api.accuweather.forecasts.DailyForecastResponse
import io.github.tobyhs.weatherweight.api.accuweather.forecasts.DayPeriod
import io.github.tobyhs.weatherweight.api.accuweather.forecasts.Temperature
import io.github.tobyhs.weatherweight.api.accuweather.forecasts.TemperatureRange
import io.github.tobyhs.weatherweight.api.accuweather.locations.AdministrativeArea
import io.github.tobyhs.weatherweight.api.accuweather.locations.City
import io.github.tobyhs.weatherweight.api.accuweather.locations.Country

import io.mockk.every
import io.mockk.mockk

import io.reactivex.rxjava3.core.Single

import java.time.Clock
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test

import retrofit2.Response

class AccuWeatherRepositoryTest {
    private val accuWeatherService = mockk<AccuWeatherService>()
    private val clock = Clock.systemUTC()
    private val repository = AccuWeatherRepository(accuWeatherService, clock)

    @Test
    fun getForecast_locationNotFound() {
        val citiesResponse = Response.success<List<City>>(emptyList())
        val citiesSingle = Single.just(citiesResponse)
        every { accuWeatherService.searchCities("Nowhere, USA", 0) } returns citiesSingle

        val observer = repository.getForecast("Nowhere, USA").test()
        observer.assertError(LocationNotFoundError::class.java)
    }

    @Test
    fun getForecast() {
        val cities: List<City> = listOf(
            City(
                key = "123456",
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
        val citiesSingle = Single.just(Response.success<List<City>>(cities))
        every { accuWeatherService.searchCities("San Jose", 0) } returns citiesSingle

        val time = ZonedDateTime.parse("2018-01-29T12:00:00Z")
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
        val response = DailyForecastResponse(dailyForecasts = dailyForecasts)
        every { accuWeatherService.get5DayForecast("123456") } returns
                Single.just(Response.success(response))
        val result = repository.getForecast("San Jose").blockingGet()

        assertThat(result.location, equalTo("San Jose, CA, US"))
        assertThat(
            result.publicationTime.until(ZonedDateTime.now(clock), ChronoUnit.MINUTES),
            equalTo(0L)
        )
        assertThat(result.forecasts.size, equalTo(2))

        var dailyForecast = result.forecasts[0]
        assertThat(dailyForecast.date.toString(), equalTo("2018-01-29"))
        assertThat(dailyForecast.low, equalTo(50))
        assertThat(dailyForecast.high, equalTo(60))
        assertThat(dailyForecast.text, equalTo("Sunny"))
        assertThat(dailyForecast.precipitationProbability, equalTo(1))

        dailyForecast = result.forecasts[1]
        assertThat(dailyForecast.date.toString(), equalTo("2018-01-30"))
        assertThat(dailyForecast.low, equalTo(52))
        assertThat(dailyForecast.high, equalTo(62))
        assertThat(dailyForecast.text, equalTo("Cloudy / Showers"))
        assertThat(dailyForecast.precipitationProbability, equalTo(70))
    }
}
