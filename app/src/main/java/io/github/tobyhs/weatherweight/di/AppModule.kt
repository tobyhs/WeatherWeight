package io.github.tobyhs.weatherweight.di

import android.content.Context

import com.squareup.moshi.Moshi

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import io.github.tobyhs.weatherweight.BuildConfig
import io.github.tobyhs.weatherweight.api.tomorrow.TomorrowApiKeyInterceptor
import io.github.tobyhs.weatherweight.api.tomorrow.TomorrowService
import io.github.tobyhs.weatherweight.data.TomorrowRepository
import io.github.tobyhs.weatherweight.data.WeatherCoroutinesRepository
import io.github.tobyhs.weatherweight.data.adapter.LocalDateAdapter
import io.github.tobyhs.weatherweight.data.adapter.ZonedDateTimeAdapter
import io.github.tobyhs.weatherweight.storage.FileLastForecastCoroutinesStore
import io.github.tobyhs.weatherweight.storage.LastForecastCoroutinesStore

import kotlinx.coroutines.Dispatchers

import java.time.Clock
import javax.inject.Singleton

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
     * @return an OkHttp client for Tomorrow.io's API
     */
    @Provides
    @Singleton
    @TomorrowApi
    fun provideTomorrowOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(TomorrowApiKeyInterceptor(BuildConfig.TOMORROW_API_KEY))
        .build()

    /**
     * @param okHttpClient HTTP client for Tomorrow.io's API
     * @param moshi Moshi instance for parsing JSON responses
     * @return a Retrofit instance for Tomorrow.io's API
     */
    @Provides
    @Singleton
    @TomorrowApi
    fun provideTomorrowRetrofit(@TomorrowApi okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.tomorrow.io/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    /**
     * @param retrofit a Retrofit instance for Tomorrow.io's API
     * @return Retrofit service object for querying weather info from Tomorrow.io's API
     */
    @Provides
    @Singleton
    fun provideTomorrowService(@TomorrowApi retrofit: Retrofit): TomorrowService = retrofit.create(
        TomorrowService::class.java
    )

    /**
     * @param service Retrofit service object for querying weather data from Tomorrow.io's API
     * @return repository object to fetch weather data
     */
    @Provides
    @Singleton
    fun provideWeatherCoroutinesRepository(
        service: TomorrowService
    ): WeatherCoroutinesRepository {
        return TomorrowRepository(service, Clock.systemDefaultZone(), Dispatchers.IO)
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
