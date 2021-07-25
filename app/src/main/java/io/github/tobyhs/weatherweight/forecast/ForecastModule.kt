package io.github.tobyhs.weatherweight.forecast

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Dagger module for [ForecastActivity]
 */
@Module
abstract class ForecastModule {
    @ContributesAndroidInjector
    abstract fun contributeForecastActivityInjector(): ForecastActivity
}
