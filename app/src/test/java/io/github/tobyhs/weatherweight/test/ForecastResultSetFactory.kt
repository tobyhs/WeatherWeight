package io.github.tobyhs.weatherweight.test

import io.github.tobyhs.weatherweight.data.model.DailyForecast
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet

import java.time.LocalDate
import java.time.ZonedDateTime

/**
 * This creates [ForecastResultSet] objects for tests
 */
object ForecastResultSetFactory {
    /**
     * @return a valid [ForecastResultSet] for testing
     */
    @JvmStatic
    fun create(): ForecastResultSet = ForecastResultSet(
        location = "Oakland, CA, US",
        publicationTime = ZonedDateTime.parse("2019-02-01T12:00:00Z"),
        forecasts = createForecasts()
    )

    /**
     * @return a list of daily forecasts for testing
     */
    @JvmStatic
    fun createForecasts(): List<DailyForecast> = listOf(
        DailyForecast(date = LocalDate.parse("2019-02-01"), low = 60, high = 65, text = "Cloudy"),
        DailyForecast(date = LocalDate.parse("2019-02-02"), low = 58, high = 64, text = "Showers"),
    )
}
