package io.github.tobyhs.weatherweight;

import android.app.Application;

import io.github.tobyhs.weatherweight.forecast.DaggerForecastComponent;
import io.github.tobyhs.weatherweight.forecast.ForecastComponent;

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
     * @return Dagger component for {@link .forecast.ForecastActivity}
     */
    public ForecastComponent createForecastComponent() {
        return DaggerForecastComponent.builder()
                .appComponent(appComponent)
                .build();
    }
}
