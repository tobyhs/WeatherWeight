package io.github.tobyhs.weatherweight.yahooweather;

import io.reactivex.Single;

import io.github.tobyhs.weatherweight.yahooweather.model.Channel;

/**
 * A repository to fetch weather/forecast data
 */
public interface WeatherRepository {
    public static final String ATTRIBUTION_URL = "https://www.yahoo.com/?ilc=401";

    /**
     * Fetches a forecast for the given location.
     *
     * @param location a string describing a location (e.g. "San Francisco, CA")
     * @return forecast data
     */
    Single<Channel> getForecast(String location);
}
