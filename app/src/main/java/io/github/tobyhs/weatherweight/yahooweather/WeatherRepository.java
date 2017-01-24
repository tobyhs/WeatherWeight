package io.github.tobyhs.weatherweight.yahooweather;

import io.reactivex.Single;

import io.github.tobyhs.weatherweight.yahooweather.model.Channel;

/**
 * A repository to fetch weather/forecast data
 */
public interface WeatherRepository {
    /**
     * Fetches a forecast for the given location.
     *
     * @param location a string describing a location (e.g. "San Francisco, CA")
     * @return forecast data
     */
    Single<Channel> getForecast(String location);
}
