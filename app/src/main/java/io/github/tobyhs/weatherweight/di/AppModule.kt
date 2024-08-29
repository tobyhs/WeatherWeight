package io.github.tobyhs.weatherweight.di

import android.content.Context

import com.squareup.moshi.Moshi

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import io.github.tobyhs.weatherweight.BuildConfig
import io.github.tobyhs.weatherweight.api.accuweather.AccuWeatherApiKeyInterceptor
import io.github.tobyhs.weatherweight.api.accuweather.AccuWeatherCoroutinesService
import io.github.tobyhs.weatherweight.data.AccuWeatherCoroutinesRepository
import io.github.tobyhs.weatherweight.data.WeatherCoroutinesRepository
import io.github.tobyhs.weatherweight.data.adapter.LocalDateAdapter
import io.github.tobyhs.weatherweight.data.adapter.ZonedDateTimeAdapter
import io.github.tobyhs.weatherweight.storage.FileLastForecastCoroutinesStore
import io.github.tobyhs.weatherweight.storage.LastForecastCoroutinesStore

import kotlinx.coroutines.Dispatchers

import java.io.File
import java.time.Clock
import javax.inject.Named
import javax.inject.Singleton

import okhttp3.Cache
import okhttp3.OkHttpClient

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Dagger module to provide common dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    /**
     * @param context application context
     * @return an OkHttp client for AccuWeather's APIs
     */
    @Provides
    @Named("accuWeatherOkHttp")
    @Singleton
    fun provideAccuWeatherOkHttp(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AccuWeatherApiKeyInterceptor(BuildConfig.ACCUWEATHER_API_KEY))
            .cache(Cache(File(context.cacheDir, "AccuWeather"), 96000))
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
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    /**
     * @param retrofit a Retrofit instance for AccuWeather's APIs
     * @return Retrofit service object for querying weather info from AccuWeather's APIs
     */
    @Provides
    @Singleton
    fun provideAccuWeatherCoroutinesService(
        @Named("accuWeatherRetrofit") retrofit: Retrofit
    ): AccuWeatherCoroutinesService = retrofit.create(AccuWeatherCoroutinesService::class.java)

    /**
     * @param service Retrofit service object for querying weather data from AccuWeather's APIs
     * @return repository object to fetch weather data
     */
    @Provides
    @Singleton
    fun provideWeatherCoroutinesRepository(
        service: AccuWeatherCoroutinesService
    ): WeatherCoroutinesRepository {
        return AccuWeatherCoroutinesRepository(service, Clock.systemDefaultZone(), Dispatchers.IO)
    }

    /**
     * @param context application context
     * @param moshi Moshi instance to serialize data
     * @return store to save or get the last forecast
     */
    @Provides
    @Singleton
    fun provideLastForecastCoroutinesStore(
        @ApplicationContext context: Context, moshi: Moshi
    ): LastForecastCoroutinesStore {
        return FileLastForecastCoroutinesStore(context.cacheDir, moshi, Dispatchers.IO)
    }

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
