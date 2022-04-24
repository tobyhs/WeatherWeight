package io.github.tobyhs.weatherweight.api.accuweather.forecasts

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Forecast details about a period of the day (day or night)
 *
 * @property iconPhrase phrase description of the icon corresponding to the forecast
 */
@JsonClass(generateAdapter = true)
data class DayPeriod(
    @Json(name = "IconPhrase") val iconPhrase: String
)
