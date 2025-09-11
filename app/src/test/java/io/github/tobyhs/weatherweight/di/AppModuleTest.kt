package io.github.tobyhs.weatherweight.di

import android.annotation.SuppressLint
import androidx.test.core.app.ApplicationProvider

import io.github.tobyhs.weatherweight.App
import io.github.tobyhs.weatherweight.api.tomorrow.TomorrowService
import io.github.tobyhs.weatherweight.data.TomorrowRepository
import io.github.tobyhs.weatherweight.storage.FileLastForecastCoroutinesStore

import io.mockk.mockk

import java.time.LocalDate
import java.time.ZonedDateTime

import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = App::class)
class AppModuleTest {
    private val app: App by lazy { ApplicationProvider.getApplicationContext() }
    private val module = AppModule()

    @Test
    fun provideWeatherCoroutinesRepository() {
        val service = mockk<TomorrowService>()
        val repo = module.provideWeatherCoroutinesRepository(service)
        assertThat(repo, instanceOf(TomorrowRepository::class.java))
    }

    @Test
    fun provideLastForecastCoroutinesStore() {
        val moshi = module.provideMoshi()
        val store = module.provideLastForecastCoroutinesStore(app, moshi)
        assertThat(store, instanceOf(FileLastForecastCoroutinesStore::class.java))
    }

    @Test
    @SuppressLint("CheckResult")
    fun provideMoshi() {
        val moshi = module.provideMoshi()
        // Check that the following don't throw an IllegalArgumentException
        moshi.adapter(LocalDate::class.java)
        moshi.adapter(ZonedDateTime::class.java)
    }
}
