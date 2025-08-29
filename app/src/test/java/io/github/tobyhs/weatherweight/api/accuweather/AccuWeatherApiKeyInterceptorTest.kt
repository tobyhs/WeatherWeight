package io.github.tobyhs.weatherweight.api.accuweather

import io.mockk.Matcher
import io.mockk.every
import io.mockk.mockk

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test

class AccuWeatherApiKeyInterceptorTest {
    @Test
    fun intercept() {
        val apiKey = "def987abc"
        val interceptor = AccuWeatherApiKeyInterceptor(apiKey)

        val chain = mockk<Interceptor.Chain>()
        val url = "https://dataservice.accuweather.com/locations/v1/cities/search?q=Springfield"
        val originalRequest = Request.Builder().get().url(url).build()
        every { chain.request() } returns originalRequest

        val expectedResponse = mockk<Response>()
        val requestMatcher = object : Matcher<Request> {
            override fun match(arg: Request?): Boolean {
                if (arg == null) return false
                return arg.method == "GET" && arg.url.toString() == "${url}&apikey=${apiKey}"
            }
        }
        every { chain.proceed(match(requestMatcher)) } returns expectedResponse
        assertThat(interceptor.intercept(chain), equalTo(expectedResponse))
    }
}
