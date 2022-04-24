package io.github.tobyhs.weatherweight.storage

import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider

import io.github.tobyhs.weatherweight.AppModule
import io.github.tobyhs.weatherweight.data.model.ForecastSearch
import io.github.tobyhs.weatherweight.test.ForecastSearchFactory

import java.io.IOException
import java.lang.RuntimeException

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.Mockito

import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SharedPrefLastForecastStoreTest {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var store: SharedPrefLastForecastStore
    private val moshi = AppModule.provideMoshi()
    private val forecastSearchAdapter = moshi.adapter(ForecastSearch::class.java)

    @Before
    fun setup() {
        sharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(ApplicationProvider.getApplicationContext())
        store = SharedPrefLastForecastStore(sharedPreferences, moshi)
    }

    @Test
    fun getWithNoEntry() {
        assertThat(store.get().blockingGet(), nullValue())
    }

    @Test
    fun getWithEntry() {
        val forecastSearch = ForecastSearchFactory.create()
        val editor = sharedPreferences.edit()
        editor.putString("lastForecast", forecastSearchAdapter.toJson(forecastSearch))
        editor.apply()

        val storedSearch = store.get().blockingGet()
        assertThat(storedSearch, equalTo(forecastSearch))
    }

    @Test
    fun saveWithCommitSuccess() {
        val forecastSearch = ForecastSearchFactory.create()
        store.save(forecastSearch).blockingAwait()

        val json = sharedPreferences.getString("lastForecast", null)!!
        val storedSearch = forecastSearchAdapter.fromJson(json)
        assertThat(storedSearch, equalTo(forecastSearch))
    }

    @Test
    fun saveWithCommitFailure() {
        val forecastSearch = ForecastSearchFactory.create()
        val sharedPreferences = Mockito.mock(SharedPreferences::class.java)
        val editor = Mockito.mock(SharedPreferences.Editor::class.java)
        Mockito.`when`(sharedPreferences.edit()).thenReturn(editor)
        Mockito.`when`(editor.commit()).thenReturn(false)
        store = SharedPrefLastForecastStore(sharedPreferences, moshi)

        val runtimeException = assertThrows(RuntimeException::class.java) {
            store.save(forecastSearch).blockingAwait()
        }
        val exception = runtimeException.cause
        assertThat(exception, instanceOf(IOException::class.java))
        assertThat(
            exception!!.message,
            equalTo("Failed to write to SharedPreferences: lastForecast")
        )
    }
}
