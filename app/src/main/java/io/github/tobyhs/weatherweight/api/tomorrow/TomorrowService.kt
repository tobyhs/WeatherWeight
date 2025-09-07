package io.github.tobyhs.weatherweight.api.tomorrow

import io.github.tobyhs.weatherweight.api.tomorrow.forecast.ForecastResponse

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for tomorrow.io's API
 */
interface TomorrowService {
    /**
     * Retrieves a forecast for the given location
     *
     * @param location location to retrieve a forecast for
     */
    @GET("v4/weather/forecast?units=imperial&timesteps=1h&timesteps=1d")
    suspend fun getForecast(@Query("location") location: String): ForecastResponse
}
