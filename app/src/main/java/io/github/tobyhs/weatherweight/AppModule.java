package io.github.tobyhs.weatherweight;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Named;
import javax.inject.Singleton;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;
import com.github.tobyhs.rxsecretary.SchedulerProvider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import io.github.tobyhs.weatherweight.storage.LastForecastStore;
import io.github.tobyhs.weatherweight.storage.SharedPrefLastForecastStore;
import io.github.tobyhs.weatherweight.util.AppSchedulerProvider;
import io.github.tobyhs.weatherweight.util.GVTypeAdapterFactory;
import io.github.tobyhs.weatherweight.yahooweather.WeatherRepository;
import io.github.tobyhs.weatherweight.yahooweather.WeatherRepositoryImpl;
import io.github.tobyhs.weatherweight.yahooweather.WeatherService;

/**
 * Dagger module to provide common dependencies
 */
@Module
public class AppModule {
    private final App app;

    /**
     * @param app application instance
     */
    public AppModule(App app) {
        this.app = app;
    }

    /**
     * @return application instance
     */
    @Provides
    @Singleton
    App provideApp() {
        return app;
    }

    /**
     * @return {@link SharedPreferences} instance for the application
     */
    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(app);
    }

    /**
     * @return RxJava scheduler provider
     */
    @Provides
    @Singleton
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }

    /**
     * @return a Gson instance
     */
    @Provides
    @Singleton
    public static Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(GVTypeAdapterFactory.create())
                .create();
    }

    /**
     * @param gson Gson instance for parsing JSON response bodies
     * @return a Retrofit instance for Yahoo's public API
     */
    @Provides @Named("yahooRetrofit")
    @Singleton
    Retrofit provideYahooRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .baseUrl("https://query.yahooapis.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(LoganSquareConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    /**
     * @param retrofit a Retrofit instance for Yahoo's public API
     * @return Retrofit service object for querying weather info from Yahoo's API
     */
    @Provides
    @Singleton
    WeatherService provideWeatherService(@Named("yahooRetrofit") Retrofit retrofit) {
        return retrofit.create(WeatherService.class);
    }

    /**
     * @param weatherService Retrofit service object for querying weather data from Yahoo's API
     * @return repository object to fetch weather data from Yahoo's API
     */
    @Provides
    @Singleton
    WeatherRepository provideWeatherRepository(WeatherService weatherService) {
        return new WeatherRepositoryImpl(weatherService);
    }

    /**
     * @param sharedPreferences {@link SharedPreferences} instance for the application
     * @param gson Gson instance to serialize data
     * @return store to save or get the last forecast
     */
    @Provides
    @Singleton
    LastForecastStore provideLastForecastStore(SharedPreferences sharedPreferences, Gson gson) {
        return new SharedPrefLastForecastStore(sharedPreferences, gson);
    }
}
