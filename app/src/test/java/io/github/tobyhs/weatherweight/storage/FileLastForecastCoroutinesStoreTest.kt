package io.github.tobyhs.weatherweight.storage

import io.github.tobyhs.weatherweight.data.model.ForecastSearch
import io.github.tobyhs.weatherweight.di.AppModule
import io.github.tobyhs.weatherweight.test.ForecastSearchFactory

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test

import java.io.File

class FileLastForecastCoroutinesStoreTest {
    private val directory = File("/tmp")
    private val moshi = AppModule().provideMoshi()
    private lateinit var testDispatcher: TestDispatcher
    private val store by lazy { FileLastForecastCoroutinesStore(directory, moshi, testDispatcher) }
    private val forecastSearchAdapter by lazy { moshi.adapter(ForecastSearch::class.java) }
    private val file = File(directory, "lastForecast.json")
    private val forecastSearch by lazy { ForecastSearchFactory.create() }

    @Test
    fun `get when file does not exist`() = setDispatcherAndRunTest {
        file.delete()
        assertThat(store.get(), nullValue())
    }

    @Test
    fun `get when file exists`() = setDispatcherAndRunTest {
        file.writeText(forecastSearchAdapter.toJson(forecastSearch))
        assertThat(store.get(), equalTo(forecastSearch))
    }

    @Test
    fun save() = setDispatcherAndRunTest {
        file.delete()
        store.save(forecastSearch)
        assertThat(forecastSearchAdapter.fromJson(file.readText()), equalTo(forecastSearch))
    }

    private fun setDispatcherAndRunTest(testBody: suspend () -> Unit) {
        runTest {
            this@FileLastForecastCoroutinesStoreTest.testDispatcher = StandardTestDispatcher(testScheduler)
            testBody()
        }
    }
}
