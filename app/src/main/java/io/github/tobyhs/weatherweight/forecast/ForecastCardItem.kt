package io.github.tobyhs.weatherweight.forecast

import java.util.Locale

import android.view.LayoutInflater
import android.view.ViewGroup

import com.mikepenz.fastadapter.binding.AbstractBindingItem

import io.github.tobyhs.weatherweight.data.model.DailyForecast
import io.github.tobyhs.weatherweight.databinding.ForecastCardBinding

import java.time.format.DateTimeFormatter
import java.time.format.TextStyle

/**
 * FastAdapter item for a [DailyForecast]
 *
 * @property forecast the [DailyForecast] to show data for
 */
class ForecastCardItem(val forecast: DailyForecast) : AbstractBindingItem<ForecastCardBinding>() {
    override val type: Int
        get() = 0

    override fun bindView(binding: ForecastCardBinding, payloads: List<Any>) {
        val locale = Locale.getDefault()
        binding.day.text = forecast.date.dayOfWeek.getDisplayName(TextStyle.SHORT, locale)
        binding.date.text = forecast.date.format(DATE_FORMATTER)
        binding.temperatureLow.text = forecast.low.toString()
        binding.temperatureHigh.text = forecast.high.toString()
        binding.description.text = forecast.text
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ForecastCardBinding {
        return ForecastCardBinding.inflate(inflater, parent, false)
    }
}

val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d")
