package io.github.tobyhs.weatherweight

import android.annotation.SuppressLint
import androidx.test.core.app.ApplicationProvider

import com.github.tobyhs.rxsecretary.android.AndroidSchedulerProvider

import io.github.tobyhs.weatherweight.api.accuweather.AccuWeatherApiKeyInterceptor
import io.github.tobyhs.weatherweight.api.accuweather.AccuWeatherService
import io.github.tobyhs.weatherweight.data.AccuWeatherRepository
import io.github.tobyhs.weatherweight.storage.FileLastForecastStore

import java.time.LocalDate
import java.time.ZonedDateTime

import okhttp3.OkHttpClient

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.isA
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.Mockito.mock

import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@RunWith(RobolectricTestRunner::class)
@Config(application = App::class)
class AppModuleTest {
    private var originalTrustStore: String? = null
    private lateinit var app: App
    private val module = AppModule()

    @Before
    fun setup() {
        originalTrustStore = System.getProperty(TRUST_STORE_PROPERTY)
        System.setProperty(TRUST_STORE_PROPERTY, "NONE")
        app = ApplicationProvider.getApplicationContext()
    }

    @After
    fun teardown() {
        if (originalTrustStore != null) {
            System.setProperty(TRUST_STORE_PROPERTY, originalTrustStore)
        }
    }

    @Test
    fun provideSchedulerProvider() {
        assertThat(
            module.provideSchedulerProvider(),
            instanceOf(AndroidSchedulerProvider::class.java)
        )
    }

    @Test
    fun provideAccuWeatherOkHttp() {
        val client = module.provideAccuWeatherOkHttp(app)
        assertThat(client.interceptors(), hasItem(isA(AccuWeatherApiKeyInterceptor::class.java)))
        assertThat(client.cache(), notNullValue())
    }

    @Test
    fun provideAccuWeatherRetrofit() {
        val client = mock(OkHttpClient::class.java)
        val retrofit = module.provideAccuWeatherRetrofit(
            client,
            AppModule.provideMoshi(),
        )
        assertThat(retrofit.baseUrl().toString(), equalTo("https://dataservice.accuweather.com/"))
        assertThat(retrofit.callFactory(), equalTo(client))
        assertThat(
            retrofit.callAdapterFactories(),
            hasItem(isA(RxJava3CallAdapterFactory::class.java))
        )
        assertThat(retrofit.converterFactories(), hasItem(isA(MoshiConverterFactory::class.java)))
    }

    @Test
    fun provideAccuWeatherService() {
        val retrofit = Retrofit.Builder().baseUrl("http://localhost/").build()
        assertThat(module.provideAccuWeatherService(retrofit), notNullValue())
    }

    @Test
    fun provideWeatherRepository() {
        val service = mock(AccuWeatherService::class.java)
        val repo = module.provideWeatherRepository(service)
        assertThat(repo, instanceOf(AccuWeatherRepository::class.java))
    }

    @Test
    fun provideLastForecastStore() {
        val moshi = AppModule.provideMoshi()
        val store = module.provideLastForecastStore(app, moshi)
        assertThat(store, instanceOf(FileLastForecastStore::class.java))

        val fileLastForecastStore = store as FileLastForecastStore
        assertThat(fileLastForecastStore.directory, equalTo(app.cacheDir))
        assertThat(fileLastForecastStore.moshi, equalTo(moshi))
    }

    @Test
    @SuppressLint("CheckResult")
    fun provideMoshi() {
        val moshi = AppModule.provideMoshi()
        // Check that the following don't throw an IllegalArgumentException
        moshi.adapter(LocalDate::class.java)
        moshi.adapter(ZonedDateTime::class.java)
    }

    companion object {
        private const val TRUST_STORE_PROPERTY = "javax.net.ssl.trustStore"
    }
}
