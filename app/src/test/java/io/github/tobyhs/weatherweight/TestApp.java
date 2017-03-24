package io.github.tobyhs.weatherweight;

import io.github.tobyhs.weatherweight.forecast.ForecastComponent;
import io.github.tobyhs.weatherweight.forecast.TestForecastComponent;

/**
 * Application class for unit tests
 */
public class TestApp extends App {
    @Override
    public ForecastComponent createForecastComponent() {
        return new TestForecastComponent();
    }
}
