package io.github.tobyhs.weatherweight.api.accuweather;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * An OkHttp interceptor to add an API key to every AccuWeather API request
 */
public class AccuWeatherApiKeyInterceptor implements Interceptor {
    private final String apiKey;

    /**
     * @param apiKey AccuWeather API key
     */
    public AccuWeatherApiKeyInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request originalRequest = chain.request();
        HttpUrl url = originalRequest.url().newBuilder().addQueryParameter("apikey", apiKey).build();
        Request newRequest = originalRequest.newBuilder().url(url).build();
        return chain.proceed(newRequest);
    }
}
