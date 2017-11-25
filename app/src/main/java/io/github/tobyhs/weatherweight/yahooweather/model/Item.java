package io.github.tobyhs.weatherweight.yahooweather.model;

import java.util.List;

import com.google.auto.value.AutoValue;

import me.tatarka.gsonvalue.annotations.GsonBuilder;

/**
 * An item contains forecast data.
 */
@AutoValue
public abstract class Item {
    /**
     * @return a list of daily forecasts for today and upcoming days
     */
    public abstract List<SingleForecast> getForecast();

    /**
     * @return publication date of forecast
     */
    public abstract String getPubDate();

    @GsonBuilder
    public static Builder builder() {
        return new AutoValue_Item.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setForecast(List<SingleForecast> forecast);
        public abstract Builder setPubDate(String pubDate);
        public abstract Item build();
    }
}
