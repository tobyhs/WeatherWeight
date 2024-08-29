package io.github.tobyhs.weatherweight.di

import android.annotation.SuppressLint
import androidx.test.core.app.ApplicationProvider

import io.github.tobyhs.weatherweight.App
import io.github.tobyhs.weatherweight.api.accuweather.AccuWeatherApiKeyInterceptor
import io.github.tobyhs.weatherweight.api.accuweather.AccuWeatherCoroutinesService
import io.github.tobyhs.weatherweight.data.AccuWeatherCoroutinesRepository
import io.github.tobyhs.weatherweight.storage.FileLastForecastCoroutinesStore

import io.mockk.mockk

import java.time.LocalDate
import java.time.ZonedDateTime

import okhttp3.OkHttpClient

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.isA
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@RunWith(RobolectricTestRunner::class)
@Config(application = App::class)
class AppModuleTest {
    private val app: App by lazy { ApplicationProvider.getApplicationContext() }
    private val module = AppModule()

    @Test
    fun provideAccuWeatherOkHttp() {
        val client = module.provideAccuWeatherOkHttp(app)
        assertThat(client.interceptors(), hasItem(isA(AccuWeatherApiKeyInterceptor::class.java)))
        assertThat(client.cache(), notNullValue())
    }

    @Test
    fun provideAccuWeatherRetrofit() {
        val client = mockk<OkHttpClient>()
        val retrofit = module.provideAccuWeatherRetrofit(
            client,
            module.provideMoshi(),
        )
        assertThat(retrofit.baseUrl().toString(), equalTo("https://dataservice.accuweather.com/"))
        assertThat(retrofit.callFactory(), equalTo(client))
        assertThat(retrofit.converterFactories(), hasItem(isA(MoshiConverterFactory::class.java)))
    }

    @Test
    fun provideAccuWeatherCoroutinesService() {
        val retrofit = Retrofit.Builder().baseUrl("http://localhost/").build()
        assertThat(module.provideAccuWeatherCoroutinesService(retrofit), notNullValue())
    }

    @Test
    fun provideWeatherCoroutinesRepository() {
        val service = mockk<AccuWeatherCoroutinesService>()
        val repo = module.provideWeatherCoroutinesRepository(service)
        assertThat(repo, instanceOf(AccuWeatherCoroutinesRepository::class.java))
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
