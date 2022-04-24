package io.github.tobyhs.weatherweight.api.accuweather.locations

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * An administrative area (state, province, etc.) from AccuWeather's Locations API
 *
 * @property id ID for this admin area
 */
@JsonClass(generateAdapter = true)
data class AdministrativeArea(
    @Json(name = "ID") val id: String
)
