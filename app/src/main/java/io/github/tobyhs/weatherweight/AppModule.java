package io.github.tobyhs.weatherweight;

import javax.inject.Named;
import javax.inject.Singleton;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

import io.github.tobyhs.weatherweight.util.SchedulerProvider;
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
     * @return RxJava scheduler provider
     */
    @Provides
    @Singleton
    SchedulerProvider provideSchedulerProvider() {
        return new SchedulerProvider();
    }

    /**
     * @return a Retrofit instance for Yahoo's public API
     */
    @Provides @Named("yahooRetrofit")
    @Singleton
    Retrofit provideYahooRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://query.yahooapis.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(LoganSquareConverterFactory.create())
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
}
