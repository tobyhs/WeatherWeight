package io.github.tobyhs.weatherweight.api.tomorrow

import io.mockk.every
import io.mockk.mockk

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test

class TomorrowApiKeyInterceptorTest {
    @Test
    fun intercept() {
        val apiKey = "abc123"
        val interceptor = TomorrowApiKeyInterceptor(apiKey)

        val chain = mockk<Interceptor.Chain>()
        val url = "https://api.tomorrow.io/v4/weather/forecast"
        val originalRequest = Request.Builder().get().url(url).build()
        every { chain.request() } returns originalRequest

        val expectedResponse = mockk<Response>()
        every {
            chain.proceed(match { request ->
                request.method == "GET" && request.url.toString() == url &&
                        request.header("apikey") == apiKey
            })
        } returns expectedResponse
        assertThat(interceptor.intercept(chain), equalTo(expectedResponse))
    }
}
