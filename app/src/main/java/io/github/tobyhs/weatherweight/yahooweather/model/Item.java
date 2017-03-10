package io.github.tobyhs.weatherweight.yahooweather.model;

import java.util.List;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * An item contains forecast data.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class Item {
    private List<SingleForecast> forecast;
    private String pubDate;

    /**
     * @return a list of daily forecasts for today and upcoming days
     */
    public List<SingleForecast> getForecast() {
        return forecast;
    }

    /**
     * @return publication date of forecast
     */
    public String getPubDate() {
        return pubDate;
    }

    public Item setForecast(List<SingleForecast> forecast) {
        this.forecast = forecast;
        return this;
    }

    public Item setPubDate(String pubDate) {
        this.pubDate = pubDate;
        return this;
    }
}
