package io.github.tobyhs.weatherweight.storage;

import io.reactivex.Completable;
import io.reactivex.Maybe;

import io.github.tobyhs.weatherweight.yahooweather.model.Channel;

/**
 * Storage to save and retrieve the last forecast data retrieved.
 */
public interface LastForecastStore {
    /**
     * Retrieves the last forecast data
     *
     * @return {@link Maybe} to subscribe to
     */
    Maybe<Channel> get();

    /**
     * Saves the given forecast data
     *
     * @param channel forecast data to save
     * @return {@link Completable} to subscribe to
     */
    Completable save(Channel channel);
}
