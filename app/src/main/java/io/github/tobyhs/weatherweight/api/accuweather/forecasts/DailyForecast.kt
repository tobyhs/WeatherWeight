package io.github.tobyhs.weatherweight.api.accuweather.forecasts

import java.time.ZonedDateTime

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * A forecast for a day from AccuWeather's Forecasts API
 *
 * @property date the date that this forecast is for
 * @property temperature the forecasted minimum and maximum temperatures
 * @property day forecast description about the day
 * @property night forecast description about the night
 */
@JsonClass(generateAdapter = true)
data class DailyForecast(
    @Json(name = "Date") val date: ZonedDateTime,
    @Json(name = "Temperature") val temperature: TemperatureRange,
    @Json(name = "Day") val day: DayPeriod,
    @Json(name = "Night") val night: DayPeriod,
)
