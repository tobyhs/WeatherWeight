package io.github.tobyhs.weatherweight.api.tomorrow.forecast

import com.squareup.moshi.JsonClass

/**
 * Forecasts for upcoming time periods
 *
 * @property hourly list of hourly forecasts
 * @property daily list of daily forecasts
 */
@JsonClass(generateAdapter = true)
data class ForecastTimelines(
    val hourly: List<HourlyForecast>,
    val daily: List<DailyForecast>,
)
