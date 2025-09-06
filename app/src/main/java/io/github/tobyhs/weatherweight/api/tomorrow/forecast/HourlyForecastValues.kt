package io.github.tobyhs.weatherweight.api.tomorrow.forecast

import com.squareup.moshi.JsonClass

/**
 * Forecast values for an hour
 *
 * @property temperature forecasted temperature
 * @property precipitationProbability percent representing the probability of precipitation
 * @property weatherCode code corresponding to a description of the weather conditions
 */
@JsonClass(generateAdapter = true)
data class HourlyForecastValues(
    val temperature: Double,
    val precipitationProbability: Int,
    val weatherCode: Int,
)
