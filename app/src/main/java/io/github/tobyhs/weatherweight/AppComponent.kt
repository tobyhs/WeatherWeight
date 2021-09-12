package io.github.tobyhs.weatherweight

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

import io.github.tobyhs.weatherweight.forecast.ForecastModule

import javax.inject.Singleton

/**
 * Dagger component for the application instance
 */
@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, ForecastModule::class])
interface AppComponent : AndroidInjector<App> {
    @Component.Factory
    interface Factory {
        fun create(appModule: AppModule): AppComponent
    }
}
