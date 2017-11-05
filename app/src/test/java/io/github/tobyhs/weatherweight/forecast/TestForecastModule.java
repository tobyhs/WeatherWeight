package io.github.tobyhs.weatherweight.forecast;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

import static org.mockito.Mockito.mock;

/**
 * Dagger module for {@link ForecastActivity} in tests
 */
@Module
public abstract class TestForecastModule {
    @ContributesAndroidInjector
    abstract ForecastActivity contributeForecastActivityInjector();

    /**
     * @return mock presenter
     */
    @Provides
    public static ForecastPresenter provideForecastPresenter() {
        return mock(ForecastPresenter.class);
    }
}
