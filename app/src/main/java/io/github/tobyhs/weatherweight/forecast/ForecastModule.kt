package io.github.tobyhs.weatherweight.forecast

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

/**
 * Dagger module for [ForecastActivity]
 */
@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ForecastModule
