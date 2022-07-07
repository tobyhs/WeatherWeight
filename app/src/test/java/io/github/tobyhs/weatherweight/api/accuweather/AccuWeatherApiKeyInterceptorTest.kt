package io.github.tobyhs.weatherweight.api.accuweather

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test

import org.mockito.kotlin.argThat
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class AccuWeatherApiKeyInterceptorTest {
    @Test
    fun intercept() {
        val apiKey = "def987abc"
        val interceptor = AccuWeatherApiKeyInterceptor(apiKey)

        val chain = mock<Interceptor.Chain>()
        val url = "https://dataservice.accuweather.com/locations/v1/cities/search?q=Springfield"
        val originalRequest = Request.Builder().get().url(url).build()
        whenever(chain.request()).thenReturn(originalRequest)

        val expectedResponse = mock<Response>()
        val requestMatcher = argThat<Request> { request ->
            request.method() == "GET" && request.url().toString() == "${url}&apikey=${apiKey}"
        }
        whenever(chain.proceed(requestMatcher)).thenReturn(expectedResponse)
        assertThat(interceptor.intercept(chain), equalTo(expectedResponse))
    }
}
