package io.github.tobyhs.weatherweight.api.tomorrow.forecast

import com.squareup.moshi.JsonClass

/**
 * Response for Tomorrow's weather forecast API
 *
 * @property timelines forecast data
 * @property location location of the forecast
 */
@JsonClass(generateAdapter = true)
data class ForecastResponse(
    val timelines: ForecastTimelines,
    val location: Location,
)
