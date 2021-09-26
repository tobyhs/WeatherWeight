package io.github.tobyhs.weatherweight.test

import io.github.tobyhs.weatherweight.data.model.DailyForecast
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet

import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime

/**
 * This creates [ForecastResultSet] objects for tests
 */
object ForecastResultSetFactory {
    /**
     * @return a valid [ForecastResultSet] for testing
     */
    @JvmStatic
    fun create(): ForecastResultSet = ForecastResultSet.builder()
        .setLocation("Oakland, CA, US")
        .setPublicationTime(ZonedDateTime.parse("2019-02-01T12:00:00Z"))
        .setForecasts(createForecasts())
        .build()

    /**
     * @return a list of daily forecasts for testing
     */
    @JvmStatic
    fun createForecasts(): List<DailyForecast> = listOf(
        DailyForecast.builder().setDate(LocalDate.parse("2019-02-01"))
            .setLow(60).setHigh(65).setText("Cloudy").build(),
        DailyForecast.builder().setDate(LocalDate.parse("2019-02-02"))
            .setLow(58).setHigh(64).setText("Showers").build()
    )
}
