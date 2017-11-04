package io.github.tobyhs.weatherweight;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * Application instance
 */
public class App extends DaggerApplication {
    @Override
    protected AndroidInjector<App> applicationInjector() {
        return DaggerAppComponent.builder().appModule(new AppModule(this)).create(this);
    }
}
