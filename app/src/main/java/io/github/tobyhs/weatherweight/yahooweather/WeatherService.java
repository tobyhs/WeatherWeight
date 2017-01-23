package io.github.tobyhs.weatherweight.yahooweather;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

import io.github.tobyhs.weatherweight.yahooweather.model.WeatherResponse;

/**
 * Retrofit service interface to get data from the Yahoo Weather API
 */
interface WeatherService {
    /**
     * Finds weather data with the given YQL.
     *
     * @param yql query to find weather by
     * @return weather data
     */
    @Headers({"Accept: application/json"})
    @GET("v1/public/yql")
    Single<Response<WeatherResponse>> getByYql(@Query("q") String yql);
}
