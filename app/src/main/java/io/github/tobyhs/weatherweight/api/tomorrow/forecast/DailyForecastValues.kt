package io.github.tobyhs.weatherweight.api.tomorrow.forecast

import com.squareup.moshi.JsonClass

/**
 * Forecast values for a day
 *
 * @property temperatureMin minimum temperature
 * @property temperatureMax maximum temperature
 * @property precipitationProbabilityMax maximum probability of precipitation
 * @property weatherCodeMin
 * @property weatherCodeMax
 */
@JsonClass(generateAdapter = true)
data class DailyForecastValues(
    val temperatureMin: Double,
    val temperatureMax: Double,
    val precipitationProbabilityMax: Int,
    val weatherCodeMin: Int,
    val weatherCodeMax: Int,
)
