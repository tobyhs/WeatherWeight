package io.github.tobyhs.weatherweight.data

import io.github.tobyhs.weatherweight.data.model.ForecastResultSet

import io.reactivex.rxjava3.core.Single

/**
 * A repository to fetch weather/forecast data
 */
interface WeatherRepository {
    /**
     * Fetches a forecast for the given location.
     *
     * @param location a string describing a location (e.g. "San Francisco, CA, USA")
     * @return forecast data
     */
    fun getForecast(location: String): Single<ForecastResultSet>
}
