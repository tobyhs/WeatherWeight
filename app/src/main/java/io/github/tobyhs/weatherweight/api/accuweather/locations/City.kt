package io.github.tobyhs.weatherweight.api.accuweather.locations

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * A city from AccuWeather's Locations API
 *
 * @property key location key
 * @property localizedName name of this city
 * @property country country of this city
 * @property administrativeArea administrative area of this city
 */
@JsonClass(generateAdapter = true)
data class City(
    @Json(name = "Key") val key: String,
    @Json(name = "LocalizedName") val localizedName: String,
    @Json(name = "Country") val country: Country,
    @Json(name = "AdministrativeArea") val administrativeArea: AdministrativeArea,
)
