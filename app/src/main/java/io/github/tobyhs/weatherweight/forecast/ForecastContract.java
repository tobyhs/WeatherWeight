package io.github.tobyhs.weatherweight.forecast;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import io.github.tobyhs.weatherweight.yahooweather.model.Channel;

/**
 * MVP interfaces for the forecast activity
 */
public interface ForecastContract {
    /**
     * View interface for displaying a forecast
     */
    interface View extends MvpLceView<Channel> {
    }
}
