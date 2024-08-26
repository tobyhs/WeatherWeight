package io.github.tobyhs.weatherweight.api.accuweather

import io.github.tobyhs.weatherweight.api.accuweather.forecasts.DailyForecastResponse
import io.github.tobyhs.weatherweight.api.accuweather.locations.City

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit interface for AccuWeather's APIs
 */
interface AccuWeatherCoroutinesService {
    /**
     * Searches for cities that match the given text
     *
     * @param query city/location to search for
     * @param offset integer that determines the first resource to be returned
     * @return a list of cities that match the given query
     */
    @GET("locations/v1/cities/search")
    suspend fun searchCities(@Query("q") query: String, @Query("offset") offset: Int): List<City>

    /**
     * Retrieves a 5-day daily forecast for the given location
     *
     * @param locationKey key of a city/location from AccuWeather's Locations API
     * @return a 5-day forecast
     */
    @GET("forecasts/v1/daily/5day/{locationKey}?details=true")
    suspend fun get5DayForecast(@Path("locationKey") locationKey: String): DailyForecastResponse
}
