package io.github.tobyhs.weatherweight.api.accuweather.forecasts

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

import java.time.ZonedDateTime

/**
 * A forecast for an hour from AccuWeather's Forecasts API
 *
 * @property dateTime date and time of the forecast
 * @property iconPhrase phrase description of the icon corresponding to the forecast
 * @property temperature the forecasted temperature for the hour
 * @property precipitationProbability percent representing the probability of precipitation
 */
@JsonClass(generateAdapter = true)
data class HourlyForecast(
    @Json(name = "DateTime") val dateTime: ZonedDateTime,
    @Json(name = "IconPhrase") val iconPhrase: String,
    @Json(name = "Temperature") val temperature: Temperature,
    @Json(name = "PrecipitationProbability") val precipitationProbability: Int,
)
