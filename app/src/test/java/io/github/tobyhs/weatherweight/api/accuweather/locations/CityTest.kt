package io.github.tobyhs.weatherweight.api.accuweather.locations

import io.github.tobyhs.weatherweight.AppModule

import java.io.InputStreamReader

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test

class CityTest {
    @Test
    fun parseJson() {
        javaClass.getResourceAsStream("/accuweather/city.json").use { stream ->
            InputStreamReader(stream).use { reader ->
                val moshi = AppModule.provideMoshi()
                val moshiAdapter = moshi.adapter(City::class.java)
                val city = moshiAdapter.fromJson(reader.readText())!!

                assertThat(city.key, equalTo("347629"))
                assertThat(city.localizedName, equalTo("San Francisco"))
                assertThat(city.administrativeArea.id, equalTo("CA"))
                assertThat(city.country.id, equalTo("US"))
            }
        }
    }
}