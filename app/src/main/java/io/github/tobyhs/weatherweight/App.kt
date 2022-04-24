package io.github.tobyhs.weatherweight

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

/**
 * Application instance
 */
open class App : DaggerApplication() {
    public override fun applicationInjector(): AndroidInjector<App> {
        return DaggerAppComponent.factory().create(AppModule(this))
    }
}
