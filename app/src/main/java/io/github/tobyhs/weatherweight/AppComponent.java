package io.github.tobyhs.weatherweight;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

import io.github.tobyhs.weatherweight.forecast.ForecastModule;

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
    @Component.Factory
    interface Factory {
        AppComponent create(AppModule appModule);
    }
}
