package io.github.tobyhs.weatherweight.api.tomorrow.forecast

import com.squareup.moshi.JsonClass

import java.time.ZonedDateTime

/**
 * Forecast for an hour
 *
 * @property time hour that the forecast is for
 * @property values forecast values
 */
@JsonClass(generateAdapter = true)
data class HourlyForecast(
    val time: ZonedDateTime,
    val values: HourlyForecastValues,
)
