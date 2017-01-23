package io.github.tobyhs.weatherweight.yahooweather.model;

import java.util.List;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * An item contains forecast data.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class Item {
    private List<SingleForecast> forecast;

    /**
     * @return a list of daily forecasts for today and upcoming days
     */
    public List<SingleForecast> getForecast() {
        return forecast;
    }

    public Item setForecast(List<SingleForecast> forecast) {
        this.forecast = forecast;
        return this;
    }
}
