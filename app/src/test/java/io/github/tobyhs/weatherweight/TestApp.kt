package io.github.tobyhs.weatherweight

import dagger.android.AndroidInjector

/**
 * Application class for unit tests
 */
class TestApp : App() {
    override fun applicationInjector(): AndroidInjector<App> {
        return DaggerTestAppComponent.factory().create(this)
    }
}
