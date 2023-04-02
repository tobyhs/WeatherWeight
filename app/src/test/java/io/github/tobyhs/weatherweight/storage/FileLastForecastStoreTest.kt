package io.github.tobyhs.weatherweight.storage

import io.github.tobyhs.weatherweight.di.AppModule
import io.github.tobyhs.weatherweight.data.model.ForecastSearch
import io.github.tobyhs.weatherweight.test.ForecastSearchFactory

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test

import java.io.File

class FileLastForecastStoreTest {
    private val directory = File("/tmp")
    private val moshi = AppModule().provideMoshi()
    private val forecastSearchAdapter = moshi.adapter(ForecastSearch::class.java)
    private val store = FileLastForecastStore(directory, moshi)
    private val file = File(directory, "lastForecast.json")
    private val forecastSearch = ForecastSearchFactory.create()

    @Test
    fun `get when file does not exist`() {
        file.delete()
        assertThat(store.get().blockingGet(), nullValue())
    }

    @Test
    fun `get when file exists`() {
        file.writeText(forecastSearchAdapter.toJson(forecastSearch))
        assertThat(store.get().blockingGet(), equalTo(forecastSearch))
    }

    @Test
    fun save() {
        file.delete()
        store.save(forecastSearch).blockingAwait()
        assertThat(forecastSearchAdapter.fromJson(file.readText()), equalTo(forecastSearch))
    }
}
