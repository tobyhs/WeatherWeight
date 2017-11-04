package io.github.tobyhs.weatherweight;

import javax.inject.Singleton;

import com.github.tobyhs.rxsecretary.SchedulerProvider;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

import io.github.tobyhs.weatherweight.forecast.ForecastModule;
import io.github.tobyhs.weatherweight.storage.LastForecastStore;
import io.github.tobyhs.weatherweight.yahooweather.WeatherRepository;

/**
 * Dagger component for the application instance
 */
@Singleton
@Component(
        modules = {
                AndroidInjectionModule.class,
                AppModule.class,
                ForecastModule.class,
        }
)
public interface AppComponent extends AndroidInjector<App> {
    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<App> {
        @Override
        public abstract AppComponent build();

        abstract Builder appModule(AppModule appModule);
    }

    SchedulerProvider schedulerProvider();
    WeatherRepository weatherRepository();
    LastForecastStore lastForecastStore();
}
