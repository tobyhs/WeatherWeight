package io.github.tobyhs.weatherweight.data.model

/**
 * This contains the input location and the resulting forecast data for a forecast search/query.
 *
 * @property input the input location entered for the search
 * @property forecastResultSet the forecast result of the search
 */
data class ForecastSearch(
    val input: String,
    val forecastResultSet: ForecastResultSet,
)
