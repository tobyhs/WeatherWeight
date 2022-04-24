package io.github.tobyhs.weatherweight.api.accuweather.forecasts

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * A temperature from AccuWeather's APIs
 *
 * @property value value of the temperature
 */
@JsonClass(generateAdapter = true)
data class Temperature(
    @Json(name = "Value") val value: Double
)
