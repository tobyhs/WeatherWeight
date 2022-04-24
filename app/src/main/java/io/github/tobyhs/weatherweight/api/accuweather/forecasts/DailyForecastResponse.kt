package io.github.tobyhs.weatherweight.api.accuweather.forecasts

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * A response from one of AccuWeather's Forecasts API endpoints
 *
 * @property dailyForecasts list of daily forecasts
 */
@JsonClass(generateAdapter = true)
data class DailyForecastResponse(
    @Json(name = "DailyForecasts") val dailyForecasts: List<DailyForecast>
)
