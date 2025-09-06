package io.github.tobyhs.weatherweight.api.tomorrow.forecast

import com.squareup.moshi.JsonClass

/**
 * Location of the forecast
 *
 * @property name name of the location
 */
@JsonClass(generateAdapter = true)
data class Location(
    val name: String,
)
