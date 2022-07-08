package io.github.tobyhs.weatherweight.data

import io.github.tobyhs.weatherweight.api.accuweather.AccuWeatherService
import io.github.tobyhs.weatherweight.api.accuweather.locations.City
import io.github.tobyhs.weatherweight.data.model.DailyForecast
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet

import io.reactivex.rxjava3.core.Single

import java.time.Clock
import java.time.ZonedDateTime

/**
 * Implementation of [WeatherRepository] that uses AccuWeather's APIs
 */
class AccuWeatherRepository
/**
 * @param accuWeatherService service to get data from AccuWeather's APIs
 * @param clock clock used to get the current instant
 */(private val accuWeatherService: AccuWeatherService, private val clock: Clock) :
    WeatherRepository {
    override fun getForecast(location: String): Single<ForecastResultSet> {
        var formattedLocation = ""
        // An explicit offset of 0 limits the results to 25 instead of 100
        return accuWeatherService.searchCities(location, 0).flatMap { citiesResponse ->
            val city: City = try {
                citiesResponse.body()!![0]
            } catch (e: IndexOutOfBoundsException) {
                throw LocationNotFoundError(location)
            }
            formattedLocation = formatCity(city)
            accuWeatherService.get5DayForecast(city.key)
        }.map { response ->
            ForecastResultSet(
                location = formattedLocation,
                publicationTime = ZonedDateTime.now(clock),
                forecasts = convertDailyForecastResponse(response.body()!!.dailyForecasts),
            )
        }
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
            )
        }
    }
}
