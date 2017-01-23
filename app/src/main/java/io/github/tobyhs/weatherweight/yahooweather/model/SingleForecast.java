package io.github.tobyhs.weatherweight.yahooweather.model;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * This models a forecast for a particular day.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class SingleForecast {
    private String date;
    private String high;
    private String low;
    private String text;

    /**
     * @return the date this forecast is for
     */
    public String getDate() {
        return date;
    }

    /**
     * @return forecasted high temperature
     */
    public String getHigh() {
        return high;
    }

    /**
     * @return forecasted low temperature
     */
    public String getLow() {
        return low;
    }

    /**
     * @return description of forecasted conditions
     */
    public String getText() {
        return text;
    }

    public SingleForecast setDate(String date) {
        this.date = date;
        return this;
    }

    public SingleForecast setHigh(String high) {
        this.high = high;
        return this;
    }

    public SingleForecast setLow(String low) {
        this.low = low;
        return this;
    }

    public SingleForecast setText(String text) {
        this.text = text;
        return this;
    }
}
