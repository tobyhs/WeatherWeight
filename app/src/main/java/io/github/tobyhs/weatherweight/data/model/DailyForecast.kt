package io.github.tobyhs.weatherweight.data.model

import org.threeten.bp.LocalDate

/**
 * A forecast for a particular day
 *
 * @property date the date that this forecast is for
 * @property low forecasted low temperature
 * @property high forecasted high temperature
 * @property text description of forecasted conditions
 */
data class DailyForecast(
    val date: LocalDate,
    val low: Int,
    val high: Int,
    val text: String,
)
