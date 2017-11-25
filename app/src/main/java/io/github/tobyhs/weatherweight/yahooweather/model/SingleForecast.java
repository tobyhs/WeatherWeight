package io.github.tobyhs.weatherweight.yahooweather.model;

import com.google.auto.value.AutoValue;

import me.tatarka.gsonvalue.annotations.GsonBuilder;

/**
 * This models a forecast for a particular day.
 */
@AutoValue
public abstract class SingleForecast {
    /**
     * @return the date this forecast is for
     */
    public abstract String getDate();

    /**
     * @return day of week for the date this forecast is for
     */
    public abstract String getDay();

    /**
     * @return forecasted high temperature
     */
    public abstract String getHigh();

    /**
     * @return forecasted low temperature
     */
    public abstract String getLow();

    /**
     * @return description of forecasted conditions
     */
    public abstract String getText();

    @GsonBuilder
    public static Builder builder() {
        return new AutoValue_SingleForecast.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setDate(String date);
        public abstract Builder setDay(String day);
        public abstract Builder setHigh(String high);
        public abstract Builder setLow(String low);
        public abstract Builder setText(String text);
        public abstract SingleForecast build();
    }
}
