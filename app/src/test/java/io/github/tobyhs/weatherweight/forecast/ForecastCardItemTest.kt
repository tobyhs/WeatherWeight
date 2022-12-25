package io.github.tobyhs.weatherweight.forecast

import android.app.Application
import android.view.LayoutInflater
import androidx.test.core.app.ApplicationProvider

import io.github.tobyhs.weatherweight.test.ForecastResultSetFactory

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ForecastCardItemTest {
    private val app: Application = ApplicationProvider.getApplicationContext()
    private val dailyForecast = ForecastResultSetFactory.createForecasts().first()
    private val forecastCardItem = ForecastCardItem(app, dailyForecast)

    @Test
    fun getType() {
        assertThat(forecastCardItem.type, equalTo(0))
    }

    @Test
    fun bindView() {
        val binding = forecastCardItem.createBinding(LayoutInflater.from(app))
        forecastCardItem.bindView(binding, emptyList())

        assertThat(binding.day.text, equalTo("Fri"))
        assertThat(binding.date.text, equalTo("Feb 1"))
        assertThat(binding.temperatureLow.text, equalTo("60"))
        assertThat(binding.temperatureHigh.text, equalTo("65"))
        assertThat(binding.precipitationProbability.text, equalTo("10%"))
        assertThat(binding.description.text, equalTo("Cloudy"))
    }
}
