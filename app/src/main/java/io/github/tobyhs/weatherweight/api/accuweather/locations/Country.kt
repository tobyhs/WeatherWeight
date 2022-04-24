package io.github.tobyhs.weatherweight.api.accuweather.locations

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * A country from AccuWeather's Locations API
 *
 * @property id ISO code for country
 */
@JsonClass(generateAdapter = true)
data class Country(
    @Json(name = "ID") val id: String
)
