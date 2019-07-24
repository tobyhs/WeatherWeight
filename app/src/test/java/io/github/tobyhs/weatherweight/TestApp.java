package io.github.tobyhs.weatherweight;

import dagger.android.AndroidInjector;

/**
 * Application class for unit tests
 */
public class TestApp extends App {
    @Override
    protected AndroidInjector<App> applicationInjector() {
        return DaggerTestAppComponent.factory().create(this);
    }
}
