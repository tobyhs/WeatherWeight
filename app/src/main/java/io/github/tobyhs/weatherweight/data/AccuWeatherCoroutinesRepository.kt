package io.github.tobyhs.weatherweight.data

import io.github.tobyhs.weatherweight.api.accuweather.AccuWeatherCoroutinesService
import io.github.tobyhs.weatherweight.api.accuweather.locations.City
import io.github.tobyhs.weatherweight.data.model.DailyForecast
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

import java.time.Clock
import java.time.ZonedDateTime

import kotlin.math.max

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
        val forecasts = accuWeatherService.get5DayForecast(city.key).dailyForecasts
        return@withContext ForecastResultSet(
            location = formatCity(city),
            publicationTime = ZonedDateTime.now(clock),
            forecasts = convertDailyForecastResponse(forecasts),
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
    private fun convertDailyForecastResponse(
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
                low = forecast.temperature.minimum.value.toInt(),
                high = forecast.temperature.maximum.value.toInt(),
                text = text,
                precipitationProbability = max(
                    forecast.day.precipitationProbability,
                    forecast.night.precipitationProbability
                ),
            )
        }
    }
}
