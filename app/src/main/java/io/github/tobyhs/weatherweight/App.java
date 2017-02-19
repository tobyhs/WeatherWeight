package io.github.tobyhs.weatherweight;

import android.app.Application;

/**
 * Application instance
 */
public class App extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    /**
     * @return Dagger component for this application
     */
    public AppComponent getAppComponent() {
        return appComponent;
    }
}
