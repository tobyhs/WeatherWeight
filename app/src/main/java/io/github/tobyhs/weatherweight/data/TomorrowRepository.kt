package io.github.tobyhs.weatherweight.data

import io.github.tobyhs.weatherweight.api.tomorrow.TomorrowService
import io.github.tobyhs.weatherweight.data.model.DailyForecast
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet
import io.github.tobyhs.weatherweight.data.model.HourlyForecast

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

import java.time.Clock
import java.time.ZonedDateTime

import kotlin.math.roundToInt

/**
 * Implementation of [WeatherCoroutinesRepository] that uses tomorrow.io' APIs
 */
class TomorrowRepository(
    private val tomorrowService: TomorrowService,
    private val clock: Clock,
    private val ioDispatcher: CoroutineDispatcher,
) : WeatherCoroutinesRepository {
    override suspend fun getForecast(location: String): ForecastResultSet = withContext(ioDispatcher) {
        val forecast = tomorrowService.getForecast(location)
        ForecastResultSet(
            location = forecast.location.name,
            publicationTime = ZonedDateTime.now(clock),
            dailyForecasts = convertDailyForecasts(forecast.timelines.daily),
            hourlyForecasts = convertHourlyForecasts(forecast.timelines.hourly),
        )
    }

    private fun convertDailyForecasts(
        dailyForecasts: List<io.github.tobyhs.weatherweight.api.tomorrow.forecast.DailyForecast>
    ): List<DailyForecast> = dailyForecasts.map { dailyForecast ->
        val descriptionMin = convertWeatherCode(dailyForecast.values.weatherCodeMin)
        val descriptionMax = convertWeatherCode(dailyForecast.values.weatherCodeMax)
        val description = if (descriptionMin == descriptionMax) {
            descriptionMin
        } else {
            "$descriptionMin / $descriptionMax"
        }
        DailyForecast(
            date = dailyForecast.time.toLocalDate(),
            low = dailyForecast.values.temperatureMin.roundToInt(),
            high = dailyForecast.values.temperatureMax.roundToInt(),
            text = description,
            precipitationProbability = dailyForecast.values.precipitationProbabilityMax,
        )
    }

    private fun convertHourlyForecasts(
        hourlyForecasts: List<io.github.tobyhs.weatherweight.api.tomorrow.forecast.HourlyForecast>
    ): List<HourlyForecast> = hourlyForecasts.map { hourlyForecast ->
        HourlyForecast(
            dateTime = hourlyForecast.time,
            text = convertWeatherCode(hourlyForecast.values.weatherCode),
            temperature = hourlyForecast.values.temperature.roundToInt(),
            precipitationProbability = hourlyForecast.values.precipitationProbability,
        )
    }

    private fun convertWeatherCode(weatherCode: Int): String {
        return TOMORROW_WEATHER_CODES.getOrDefault(weatherCode, "Unknown")
    }
}

// From https://docs.tomorrow.io/reference/data-layers-weather-codes
private val TOMORROW_WEATHER_CODES = mapOf(
    0 to "Unknown",
    1000 to "Clear, Sunny",
    1100 to "Mostly Clear",
    1101 to "Partly Cloudy",
    1102 to "Mostly Cloudy",
    1001 to "Cloudy",
    2000 to "Fog",
    2100 to "Light Fog",
    4000 to "Drizzle",
    4001 to "Rain",
    4200 to "Light Rain",
    4201 to "Heavy Rain",
    5000 to "Snow",
    5001 to "Flurries",
    5100 to "Light Snow",
    5101 to "Heavy Snow",
    6000 to "Freezing Drizzle",
    6001 to "Freezing Rain",
    6200 to "Light Freezing Rain",
    6201 to "Heavy Freezing Rain",
    7000 to "Ice Pellets",
    7101 to "Heavy Ice Pellets",
    7102 to "Light Ice Pellets",
    8000 to "Thunderstorm",
)
