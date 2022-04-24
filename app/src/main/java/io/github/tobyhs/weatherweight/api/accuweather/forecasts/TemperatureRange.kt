package io.github.tobyhs.weatherweight.api.accuweather.forecasts

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * A range of temperatures for a daily forecast
 *
 * @property minimum the forecasted minimum temperature for a day
 * @property maximum the forecasted maximum temperature for a day
 */
@JsonClass(generateAdapter = true)
data class TemperatureRange(
    @Json(name = "Minimum") val minimum: Temperature,
    @Json(name = "Maximum") val maximum: Temperature,
)
