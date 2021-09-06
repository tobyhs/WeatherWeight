package io.github.tobyhs.weatherweight.storage

import io.github.tobyhs.weatherweight.data.model.ForecastSearch

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

/**
 * Storage to save and retrieve the last forecast data retrieved.
 */
interface LastForecastStore {
    /**
     * Retrieves the last forecast data
     *
     * @return [Maybe] to subscribe to
     */
    fun get(): Maybe<ForecastSearch>

    /**
     * Saves the given forecast data
     *
     * @param forecastSearch forecast data to save
     * @return [Completable] to subscribe to
     */
    fun save(forecastSearch: ForecastSearch): Completable
}
