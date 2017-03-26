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
        /**
         * Sets the location input's text to the given string.
         *
         * @param location string to set to
         */
        void setLocationInputText(String location);
    }
}
