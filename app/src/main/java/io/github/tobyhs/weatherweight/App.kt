package io.github.tobyhs.weatherweight

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

import com.jakewharton.threetenabp.AndroidThreeTen

/**
 * Application instance
 */
open class App : DaggerApplication() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }

    public override fun applicationInjector(): AndroidInjector<App> {
        return DaggerAppComponent.factory().create(AppModule(this))
    }
}
