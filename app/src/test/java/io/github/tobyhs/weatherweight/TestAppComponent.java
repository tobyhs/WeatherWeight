package io.github.tobyhs.weatherweight;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

import io.github.tobyhs.weatherweight.forecast.TestForecastModule;

/**
 * Dagger component for {@link TestApp}
 */
@Singleton
@Component(
        modules = {
                AndroidInjectionModule.class,
                TestForecastModule.class,
        }
)
public interface TestAppComponent extends AndroidInjector<App> {
    @Component.Factory
    interface Factory extends AndroidInjector.Factory<App> {}
}
