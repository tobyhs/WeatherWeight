package io.github.tobyhs.weatherweight.forecast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.github.tobyhs.weatherweight.App;
import io.github.tobyhs.weatherweight.R;

/**
 * Main activity to enter a location to retrieve a weather forecast
 */
public class ForecastActivity extends AppCompatActivity {
    private ForecastComponent forecastComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        forecastComponent = DaggerForecastComponent.builder()
                .appComponent(((App) getApplication()).getAppComponent())
                .build();
    }
}
