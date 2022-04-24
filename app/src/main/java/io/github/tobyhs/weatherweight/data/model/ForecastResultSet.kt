package io.github.tobyhs.weatherweight.data.model

import com.squareup.moshi.JsonClass

import java.time.ZonedDateTime

/**
 * A result set for weather forecasts
 *
 * @property location location this forecast is for
 * @property publicationTime time of this forecast
 * @property forecasts daily forecasts for today and upcoming days
 */
@JsonClass(generateAdapter = true)
data class ForecastResultSet(
    val location: String,
    val publicationTime: ZonedDateTime,
    val forecasts: List<DailyForecast>,
)
