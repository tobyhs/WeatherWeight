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

import io.reactivex.rxjava3.core.Single

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Before
import org.junit.Test

import org.mockito.Mockito
import org.mockito.Mockito.mock

import org.threeten.bp.Clock
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit

import retrofit2.Response

class AccuWeatherRepositoryTest {
    private lateinit var accuWeatherService: AccuWeatherService
    private val clock = Clock.systemUTC()
    private lateinit var repository: AccuWeatherRepository

    @Before
    fun setup() {
        accuWeatherService = mock(AccuWeatherService::class.java)
        repository = AccuWeatherRepository(accuWeatherService, clock)
    }

    @Test
    fun getForecast_locationNotFound() {
        val citiesResponse = Response.success<List<City>>(emptyList())
        val citiesSingle = Single.just(citiesResponse)
        Mockito.`when`(accuWeatherService.searchCities("Nowhere, USA", 0)).thenReturn(citiesSingle)

        val observer = repository.getForecast("Nowhere, USA").test()
        observer.assertError(LocationNotFoundError::class.java)
    }

    @Test
    fun getForecast() {
        val cities: MutableList<City> = mutableListOf()
        cities.add(
            City.builder()
                .setKey("123456")
                .setLocalizedName("San Jose")
                .setAdministrativeArea(AdministrativeArea.builder().setId("CA").build())
                .setCountry(Country.builder().setId("US").build())
                .build()
        )
        cities.add(
            City.builder()
                .setKey("432165")
                .setLocalizedName("San Jose")
                .setAdministrativeArea(AdministrativeArea.builder().setId("SJ").build())
                .setCountry(Country.builder().setId("CR").build())
                .build()
        )
        val citiesSingle = Single.just(Response.success<List<City>>(cities))
        Mockito.`when`(accuWeatherService.searchCities("San Jose", 0)).thenReturn(citiesSingle)

        val time = ZonedDateTime.parse("2018-01-29T12:00:00Z")
        val dailyForecasts: MutableList<DailyForecast> = mutableListOf()
        var temperatureRange = TemperatureRange.builder()
            .setMinimum(Temperature.builder().setValue(50.0).build())
            .setMaximum(Temperature.builder().setValue(60.0).build())
            .build()
        dailyForecasts.add(
            DailyForecast.builder()
                .setDate(time)
                .setTemperature(temperatureRange)
                .setDay(DayPeriod.builder().setIconPhrase("Sunny").build())
                .setNight(DayPeriod.builder().setIconPhrase("Sunny").build())
                .build()
        )
        temperatureRange = TemperatureRange.builder()
            .setMinimum(Temperature.builder().setValue(52.0).build())
            .setMaximum(Temperature.builder().setValue(62.0).build())
            .build()
        dailyForecasts.add(
            DailyForecast.builder()
                .setDate(time.plusDays(1))
                .setTemperature(temperatureRange)
                .setDay(DayPeriod.builder().setIconPhrase("Cloudy").build())
                .setNight(DayPeriod.builder().setIconPhrase("Showers").build())
                .build()
        )
        val response = DailyForecastResponse.builder()
            .setDailyForecasts(dailyForecasts)
            .build()
        Mockito.`when`(accuWeatherService.get5DayForecast("123456"))
            .thenReturn(Single.just(Response.success(response)))
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

        dailyForecast = result.forecasts[1]
        assertThat(dailyForecast.date.toString(), equalTo("2018-01-30"))
        assertThat(dailyForecast.low, equalTo(52))
        assertThat(dailyForecast.high, equalTo(62))
        assertThat(dailyForecast.text, equalTo("Cloudy / Showers"))
    }
}
