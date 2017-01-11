package io.github.tobyhs.weatherweight.forecast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.github.tobyhs.weatherweight.R;

/**
 * Main activity to enter a location to retrieve a weather forecast
 */
public class ForecastActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
    }
}
