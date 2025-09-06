package io.github.tobyhs.weatherweight.api.tomorrow.forecast

import com.squareup.moshi.JsonClass

import java.time.ZonedDateTime

/**
 * Forecast for a day
 *
 * @property time day that the forecast is for
 * @property values forecast values
 */
@JsonClass(generateAdapter = true)
data class DailyForecast(
    val time: ZonedDateTime,
    val values: DailyForecastValues,
)
