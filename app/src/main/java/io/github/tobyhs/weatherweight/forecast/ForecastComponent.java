package io.github.tobyhs.weatherweight.forecast;

import dagger.Component;

import io.github.tobyhs.weatherweight.AppComponent;
import io.github.tobyhs.weatherweight.util.ActivityScoped;

/**
 * Dagger component for the forecast activity
 */
@ActivityScoped
@Component(dependencies = AppComponent.class)
public interface ForecastComponent {
    /**
     * @return presenter for the forecast activity
     */
    ForecastPresenter forecastPresenter();
}
