package io.github.tobyhs.weatherweight.forecast

import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.testing.TestInstallIn

import org.mockito.Mockito.mock

/**
 * Dagger module for [ForecastActivity] in tests
 */
@Module
@TestInstallIn(components = [ActivityRetainedComponent::class], replaces = [ForecastModule::class])
class TestForecastModule {
    /**
     * @return mock presenter
     */
    @Provides
    fun provideForecastPresenter(): ForecastPresenter = mock(ForecastPresenter::class.java)
}
