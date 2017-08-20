package io.github.tobyhs.weatherweight;

import javax.inject.Singleton;

import com.github.tobyhs.rxsecretary.SchedulerProvider;
import dagger.Component;

import io.github.tobyhs.weatherweight.storage.LastForecastStore;
import io.github.tobyhs.weatherweight.yahooweather.WeatherRepository;

/**
 * Dagger component for the application instance
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    SchedulerProvider schedulerProvider();
    WeatherRepository weatherRepository();
    LastForecastStore lastForecastStore();
}
