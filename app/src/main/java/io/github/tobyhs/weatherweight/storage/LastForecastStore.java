package io.github.tobyhs.weatherweight.storage;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;

import io.github.tobyhs.weatherweight.data.model.ForecastSearch;

/**
 * Storage to save and retrieve the last forecast data retrieved.
 */
public interface LastForecastStore {
    /**
     * Retrieves the last forecast data
     *
     * @return {@link Maybe} to subscribe to
     */
    Maybe<ForecastSearch> get();

    /**
     * Saves the given forecast data
     *
     * @param forecastSearch forecast data to save
     * @return {@link Completable} to subscribe to
     */
    Completable save(ForecastSearch forecastSearch);
}
