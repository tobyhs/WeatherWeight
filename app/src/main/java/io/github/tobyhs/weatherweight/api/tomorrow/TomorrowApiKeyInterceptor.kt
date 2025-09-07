package io.github.tobyhs.weatherweight.api.tomorrow

import okhttp3.Interceptor
import okhttp3.Response

/**
 * An OkHttp interceptor to add an API key request header to tomorrow.io API requests
 *
 * @param apiKey tomorrow.io API key
 */
class TomorrowApiKeyInterceptor(private val apiKey: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder().header("apikey", apiKey).build()
        return chain.proceed(newRequest)
    }
}