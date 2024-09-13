package io.github.tobyhs.weatherweight.data.model

import com.squareup.moshi.JsonClass

import java.time.ZonedDateTime

/**
 * A forecast for an hour
 *
 * @property dateTime hour that the forecast is for
 * @property text description of forecasted conditions
 * @property temperature forecasted temperature
 * @property precipitationProbability percent representing the probability of precipitation
 */
@JsonClass(generateAdapter = true)
data class HourlyForecast(
    val dateTime: ZonedDateTime,
    val text: String,
    val temperature: Int,
    val precipitationProbability: Int,
)
