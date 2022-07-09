package io.github.tobyhs.weatherweight.api.accuweather.forecasts

import java.io.InputStreamReader
import java.time.format.DateTimeFormatter

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test

import io.github.tobyhs.weatherweight.di.AppModule

class DailyForecastResponseTest {
    @Test
    fun parseJson() {
        javaClass.getResourceAsStream("/accuweather/dailyForecastResponse.json").use { stream ->
            InputStreamReader(stream).use { reader ->
                val moshi = AppModule.provideMoshi()
                val moshiAdapter = moshi.adapter(DailyForecastResponse::class.java)
                val dailyForecastResponse = moshiAdapter.fromJson(reader.readText())!!
                val dailyForecasts = dailyForecastResponse.dailyForecasts
                assertThat(dailyForecasts.size, equalTo(1))

                val dailyForecast = dailyForecasts[0]
                assertThat(
                    dailyForecast.date.format(DateTimeFormatter.ISO_LOCAL_DATE),
                    equalTo("2019-01-11")
                )
                assertThat(dailyForecast.temperature.minimum.value, equalTo(52.0))
                assertThat(dailyForecast.temperature.maximum.value, equalTo(57.0))
                assertThat(dailyForecast.day.iconPhrase, equalTo("Showers"))
                assertThat(dailyForecast.day.precipitationProbability, equalTo(80))
                assertThat(dailyForecast.night.iconPhrase, equalTo("Rain"))
                assertThat(dailyForecast.night.precipitationProbability, equalTo(60))
            }
        }
    }
}
