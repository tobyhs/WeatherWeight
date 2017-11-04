package io.github.tobyhs.weatherweight.forecast;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Dagger module for {@link ForecastActivity}
 */
@Module
public abstract class ForecastModule {
    @ContributesAndroidInjector
    abstract ForecastActivity contributeForecastActivityInjector();
}
