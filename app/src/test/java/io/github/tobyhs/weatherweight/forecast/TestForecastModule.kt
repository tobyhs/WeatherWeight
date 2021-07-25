package io.github.tobyhs.weatherweight.forecast

import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

import org.mockito.Mockito.mock

/**
 * Dagger module for [ForecastActivity] in tests
 */
@Module
abstract class TestForecastModule {
    @ContributesAndroidInjector
    abstract fun contributeForecastActivityInjector(): ForecastActivity

    companion object {
        /**
         * @return mock presenter
         */
        @Provides
        fun provideForecastPresenter(): ForecastPresenter {
            return mock(ForecastPresenter::class.java)
        }
    }
}
