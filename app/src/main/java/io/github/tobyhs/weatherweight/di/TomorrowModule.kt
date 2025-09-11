package io.github.tobyhs.weatherweight.di

import com.squareup.moshi.Moshi

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import io.github.tobyhs.weatherweight.BuildConfig
import io.github.tobyhs.weatherweight.api.tomorrow.TomorrowApiKeyInterceptor
import io.github.tobyhs.weatherweight.api.tomorrow.TomorrowService

import okhttp3.OkHttpClient

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

import javax.inject.Singleton

/**
 * Dagger module that provides Tomorrow.io API dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
internal object TomorrowModule {
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
}
