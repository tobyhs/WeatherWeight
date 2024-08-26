package io.github.tobyhs.weatherweight.storage

import io.github.tobyhs.weatherweight.data.model.ForecastSearch

/**
 * Storage to save and get the last forecast data retrieved.
 */
interface LastForecastCoroutinesStore {
    /**
     * Retrieves the last forecast data
     *
     * @return the last forecast data
     */
    suspend fun get(): ForecastSearch?

    /**
     * Saves the given forecast data
     *
     * @param forecastSearch forecast data to save
     */
    suspend fun save(forecastSearch: ForecastSearch)
}
