package io.github.tobyhs.weatherweight.api.accuweather.forecasts

import io.github.tobyhs.weatherweight.di.AppModule

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test

import java.io.InputStreamReader
import java.time.ZonedDateTime

class HourlyForecastTest {
    @Test
    fun parseJson() {
        javaClass.getResourceAsStream("/accuweather/hourlyForecast.json").use { stream ->
            InputStreamReader(stream).use { reader ->
                val moshi = AppModule().provideMoshi()
                val moshiAdapter = moshi.adapter(HourlyForecast::class.java)
                val hourlyForecast = moshiAdapter.fromJson(reader.readText())!!

                assertThat(hourlyForecast, equalTo(HourlyForecast(
                    dateTime = ZonedDateTime.parse("2024-09-12T04:00:00-07:00"),
                    iconPhrase = "Mostly clear",
                    temperature = Temperature(value = 58.0),
                    precipitationProbability = 3,
                )))
            }
        }
    }
}
