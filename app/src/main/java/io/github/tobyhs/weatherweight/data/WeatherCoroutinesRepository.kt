package io.github.tobyhs.weatherweight.data

import io.github.tobyhs.weatherweight.data.model.ForecastResultSet

/**
 * A repository to fetch weather/forecast data
 */
interface WeatherCoroutinesRepository {
    /**
     * Fetches a forecast for the given location.
     *
     * @param location a string describing a location (e.g. "San Francisco, CA, USA")
     * @return forecast data
     */
    suspend fun getForecast(location: String): ForecastResultSet
}
