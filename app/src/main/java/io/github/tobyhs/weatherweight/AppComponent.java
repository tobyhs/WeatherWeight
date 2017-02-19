package io.github.tobyhs.weatherweight;

import javax.inject.Singleton;

import dagger.Component;

import io.github.tobyhs.weatherweight.util.SchedulerProvider;
import io.github.tobyhs.weatherweight.yahooweather.WeatherRepository;

/**
 * Dagger component for the application instance
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    SchedulerProvider schedulerProvider();
    WeatherRepository weatherRepository();
}
