package io.github.tobyhs.weatherweight.api.accuweather

import okhttp3.Interceptor
import okhttp3.Response

/**
 * An OkHttp interceptor to add an API key to every AccuWeather API request
 *
 * @param apiKey AccuWeather API key
 */
class AccuWeatherApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.newBuilder().addQueryParameter("apikey", apiKey).build()
        val newRequest = originalRequest.newBuilder().url(url).build()
        return chain.proceed(newRequest)
    }
}
