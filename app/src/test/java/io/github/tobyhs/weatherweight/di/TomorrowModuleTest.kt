package io.github.tobyhs.weatherweight.di

import io.github.tobyhs.weatherweight.api.tomorrow.TomorrowApiKeyInterceptor

import io.mockk.mockk

import okhttp3.OkHttpClient

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.isA
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class TomorrowModuleTest {
    @Test
    fun provideTomorrowOkHttp() {
        val client = TomorrowModule.provideTomorrowOkHttp()
        assertThat(client.interceptors, hasItem(isA(TomorrowApiKeyInterceptor::class.java)))
    }

    @Test
    fun provideTomorrowRetrofit() {
        val client = mockk<OkHttpClient>()
        val retrofit = TomorrowModule.provideTomorrowRetrofit(
            client,
            AppModule().provideMoshi(),
        )
        assertThat(retrofit.baseUrl().toString(), equalTo("https://api.tomorrow.io/"))
        assertThat(retrofit.callFactory(), equalTo(client))
        assertThat(retrofit.converterFactories(), hasItem(isA(MoshiConverterFactory::class.java)))
    }

    @Test
    fun provideTomorrowService() {
        val retrofit = Retrofit.Builder().baseUrl("http://localhost/").build()
        assertThat(TomorrowModule.provideTomorrowService(retrofit), notNullValue())
    }
}
