package io.github.tobyhs.weatherweight

import com.github.tobyhs.rxsecretary.SchedulerProvider
import com.github.tobyhs.rxsecretary.android.AndroidSchedulerProvider

import com.squareup.moshi.Moshi

import dagger.Module
import dagger.Provides

import io.github.tobyhs.weatherweight.api.accuweather.AccuWeatherApiKeyInterceptor
import io.github.tobyhs.weatherweight.api.accuweather.AccuWeatherService
import io.github.tobyhs.weatherweight.data.AccuWeatherRepository
import io.github.tobyhs.weatherweight.data.WeatherRepository
import io.github.tobyhs.weatherweight.data.adapter.LocalDateAdapter
import io.github.tobyhs.weatherweight.data.adapter.ZonedDateTimeAdapter
import io.github.tobyhs.weatherweight.storage.FileLastForecastStore
import io.github.tobyhs.weatherweight.storage.LastForecastStore

import java.io.File
import java.time.Clock
import javax.inject.Named
import javax.inject.Singleton

import okhttp3.Cache
import okhttp3.OkHttpClient

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Dagger module to provide common dependencies
 */
@Module
class AppModule
/**
 * @param app application instance
 */(private val app: App) {
    /**
     * @return application instance
     */
    @Provides
    @Singleton
    fun provideApp(): App = app

    /**
     * @return RxJava scheduler provider
     */
    @Provides
    @Singleton
    fun provideSchedulerProvider(): SchedulerProvider = AndroidSchedulerProvider()

    /**
     * @return an OkHttp client for AccuWeather's APIs
     */
    @Provides
    @Named("accuWeatherOkHttp")
    @Singleton
    fun provideAccuWeatherOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AccuWeatherApiKeyInterceptor(BuildConfig.ACCUWEATHER_API_KEY))
            .cache(Cache(File(app.cacheDir, "AccuWeather"), 96000))
            .build()
    }

    /**
     * @param okHttpClient HTTP client for AccuWeather's APIs
     * @param moshi Moshi instance for parsing JSON responses
     * @return a Retrofit instance for AccuWeather's APIs
     */
    @Provides
    @Named("accuWeatherRetrofit")
    @Singleton
    fun provideAccuWeatherRetrofit(
        @Named("accuWeatherOkHttp") okHttpClient: OkHttpClient,
        moshi: Moshi,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dataservice.accuweather.com/")
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    /**
     * @param retrofit a Retrofit instance for AccuWeather's APIs
     * @return Retrofit service object for querying weather info from AccuWeather's APIs
     */
    @Provides
    @Singleton
    fun provideAccuWeatherService(@Named("accuWeatherRetrofit") retrofit: Retrofit): AccuWeatherService {
        return retrofit.create(AccuWeatherService::class.java)
    }

    /**
     * @param service Retrofit service object for querying weather data from AccuWeather's APIs
     * @return repository object to fetch weather data
     */
    @Provides
    @Singleton
    fun provideWeatherRepository(service: AccuWeatherService): WeatherRepository {
        return AccuWeatherRepository(service, Clock.systemDefaultZone())
    }

    /**
     * @param moshi Moshi instance to serialize data
     * @return store to save or get the last forecast
     */
    @Provides
    @Singleton
    fun provideLastForecastStore(moshi: Moshi): LastForecastStore {
        return FileLastForecastStore(app.cacheDir, moshi)
    }

    companion object {
        /**
         * @return a Moshi instance
         */
        @Provides
        @Singleton
        fun provideMoshi(): Moshi = Moshi.Builder()
            .add(LocalDateAdapter())
            .add(ZonedDateTimeAdapter())
            .build()
    }
}
