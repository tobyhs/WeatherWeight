package io.github.tobyhs.weatherweight.test

import io.github.tobyhs.weatherweight.data.model.DailyForecast
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet
import io.github.tobyhs.weatherweight.data.model.HourlyForecast

import java.time.LocalDate
import java.time.ZonedDateTime

/**
 * This creates [ForecastResultSet] objects for tests
 */
object ForecastResultSetFactory {
    /**
     * @return a valid [ForecastResultSet] for testing
     */
    fun create(): ForecastResultSet = ForecastResultSet(
        location = "Oakland, CA, US",
        publicationTime = ZonedDateTime.parse("2019-02-01T12:00:00Z"),
        dailyForecasts = createDailyForecasts(),
        hourlyForecasts = createHourlyForecasts(),
    )

    /**
     * @return a list of daily forecasts for testing
     */
    fun createDailyForecasts(): List<DailyForecast> = listOf(
        DailyForecast(
            date = LocalDate.parse("2019-02-01"),
            low = 60,
            high = 65,
            text = "Cloudy",
            precipitationProbability = 10,
        ),
        DailyForecast(
            date = LocalDate.parse("2019-02-02"),
            low = 58,
            high = 64,
            text = "Showers",
            precipitationProbability = 70,
        ),
    )

    /**
     * @return a list of hourly forecasts for testing
     */
    fun createHourlyForecasts(): List<HourlyForecast> = listOf(
        HourlyForecast(
            dateTime = ZonedDateTime.parse("2019-02-01T11:00:00Z"),
            text = "Mostly clear",
            temperature = 63,
            precipitationProbability = 5,
        ),
        HourlyForecast(
            dateTime = ZonedDateTime.parse("2019-02-01T12:00:00Z"),
            text = "Cloudy",
            temperature = 67,
            precipitationProbability = 10,
        )
    )
}
