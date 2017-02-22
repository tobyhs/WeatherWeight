package io.github.tobyhs.weatherweight.forecast;

import javax.inject.Inject;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import io.github.tobyhs.weatherweight.util.SchedulerProvider;
import io.github.tobyhs.weatherweight.yahooweather.WeatherRepository;

/**
 * Presenter for the forecast activity
 */
public class ForecastPresenter extends MvpBasePresenter<ForecastContract.View> {
    private final SchedulerProvider schedulerProvider;
    private final WeatherRepository weatherRepository;

    /**
     * @param schedulerProvider RX scheduler provider
     * @param weatherRepository weather repository object to obtain forecast data
     */
    @Inject
    public ForecastPresenter(SchedulerProvider schedulerProvider, WeatherRepository weatherRepository) {
        this.schedulerProvider = schedulerProvider;
        this.weatherRepository = weatherRepository;
    }
}
