package io.github.tobyhs.weatherweight;

import com.jakewharton.threetenabp.AndroidThreeTen;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * Application instance
 */
public class App extends DaggerApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }

    @Override
    protected AndroidInjector<App> applicationInjector() {
        return DaggerAppComponent.factory().create(new AppModule(this));
    }
}
