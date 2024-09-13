package io.github.tobyhs.weatherweight.data

import io.github.tobyhs.weatherweight.api.accuweather.AccuWeatherCoroutinesService
import io.github.tobyhs.weatherweight.api.accuweather.locations.City
import io.github.tobyhs.weatherweight.data.model.DailyForecast
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet
import io.github.tobyhs.weatherweight.data.model.HourlyForecast

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

import java.time.Clock
import java.time.ZonedDateTime

import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Implementation of [WeatherCoroutinesRepository] that uses AccuWeather's APIs
 */
class AccuWeatherCoroutinesRepository(
    private val accuWeatherService: AccuWeatherCoroutinesService,
    private val clock: Clock,
    private val ioDispatcher: CoroutineDispatcher,
) : WeatherCoroutinesRepository {
    override suspend fun getForecast(location: String): ForecastResultSet = withContext(ioDispatcher) {
        val cities = accuWeatherService.searchCities(location, 0)
        if (cities.isEmpty()) {
            throw LocationNotFoundError(location)
        }
        val city = cities.first()

        val dailyForecastsResponse = async { accuWeatherService.get5DayForecast(city.key) }
        val hourlyForecasts = async { accuWeatherService.get12HourForecast(city.key) }
        ForecastResultSet(
            location = formatCity(city),
            publicationTime = ZonedDateTime.now(clock),
            dailyForecasts = convertDailyForecasts(dailyForecastsResponse.await().dailyForecasts),
            hourlyForecasts = convertHourlyForecasts(hourlyForecasts.await()),
        )
    }

    /**
     * Derives a location string from a city from AccuWeather's API
     *
     * @param city a city from AccuWeather's Locations API
     * @return a location string with the city name, state/province, and country
     */
    private fun formatCity(city: City): String {
        return "${city.localizedName}, ${city.administrativeArea.id}, ${city.country.id}"
    }

    /**
     * Converts daily forecasts from AccuWeather's Forecasts API
     *
     * @param dailyForecasts daily forecasts from AccuWeather's Forecasts API
     * @return converted daily forecasts
     */
    private fun convertDailyForecasts(
        dailyForecasts: List<io.github.tobyhs.weatherweight.api.accuweather.forecasts.DailyForecast>
    ): List<DailyForecast> {
        return dailyForecasts.map { forecast ->
            var text = forecast.day.iconPhrase
            val nightPhrase = forecast.night.iconPhrase
            if (text != nightPhrase) {
                text += " / $nightPhrase"
            }
            DailyForecast(
                date = forecast.date.toLocalDate(),
                low = forecast.temperature.minimum.value.roundToInt(),
                high = forecast.temperature.maximum.value.roundToInt(),
                text = text,
                precipitationProbability = max(
                    forecast.day.precipitationProbability,
                    forecast.night.precipitationProbability
                ),
            )
        }
    }

    private fun convertHourlyForecasts(
        hourlyForecasts: List<io.github.tobyhs.weatherweight.api.accuweather.forecasts.HourlyForecast>
    ): List<HourlyForecast> = hourlyForecasts.map { forecast ->
        HourlyForecast(
            dateTime = forecast.dateTime,
            text = forecast.iconPhrase,
            temperature = forecast.temperature.value.roundToInt(),
            precipitationProbability = forecast.precipitationProbability,
        )
    }
}
