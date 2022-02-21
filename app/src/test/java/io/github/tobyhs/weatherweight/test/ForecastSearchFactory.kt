package io.github.tobyhs.weatherweight.test

import io.github.tobyhs.weatherweight.data.model.ForecastSearch

/**
 * This contains method(s) for creating [ForecastSearch]s for tests
 */
object ForecastSearchFactory {
    /**
     * @return a valid [ForecastSearch] for tests
     */
    fun create(): ForecastSearch = ForecastSearch(
        input = "Oakland, CA",
        forecastResultSet = ForecastResultSetFactory.create(),
    )
}
