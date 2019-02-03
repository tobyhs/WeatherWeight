package io.github.tobyhs.weatherweight.forecast;

import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;

import io.github.tobyhs.weatherweight.data.model.ForecastResultSet;

/**
 * MVP interfaces for the forecast activity
 */
public interface ForecastContract {
    /**
     * View interface for displaying a forecast
     */
    interface View extends MvpLceView<ForecastResultSet> {
        /**
         * Sets the location input's text to the given string.
         *
         * @param location string to set to
         */
        void setLocationInputText(String location);
    }
}
