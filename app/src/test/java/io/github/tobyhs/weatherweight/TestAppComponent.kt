package io.github.tobyhs.weatherweight

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

import io.github.tobyhs.weatherweight.forecast.TestForecastModule

import javax.inject.Singleton

/**
 * Dagger component for [TestApp]
 */
@Singleton
@Component(modules = [AndroidInjectionModule::class, TestForecastModule::class])
interface TestAppComponent : AndroidInjector<App> {
    @Component.Factory
    interface Factory : AndroidInjector.Factory<App>
}
