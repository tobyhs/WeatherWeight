package io.github.tobyhs.weatherweight.yahooweather.model;

import com.google.auto.value.AutoValue;

import me.tatarka.gsonvalue.annotations.GsonBuilder;

/**
 * Weather data for one location
 */
@AutoValue
public abstract class Channel {
    /**
     * @return location the forecast is for
     */
    public abstract Location getLocation();

    /**
     * @return entry that contains conditions and forecasts
     */
    public abstract Item getItem();

    /**
     * @return link to corresponding Yahoo weather page for the location
     */
    public abstract String getLink();

    @GsonBuilder
    public static Builder builder() {
        return new AutoValue_Channel.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setLocation(Location location);
        public abstract Builder setItem(Item item);
        public abstract Builder setLink(String link);
        public abstract Channel build();
    }
}
