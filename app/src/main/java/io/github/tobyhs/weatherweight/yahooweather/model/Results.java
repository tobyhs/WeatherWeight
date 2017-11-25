package io.github.tobyhs.weatherweight.yahooweather.model;

import com.google.auto.value.AutoValue;

import me.tatarka.gsonvalue.annotations.GsonBuilder;

/**
 * Resulting data from a Yahoo Weather API query
 */
@AutoValue
public abstract class Results {
    /**
     * @return weather data for one location
     */
    public abstract Channel getChannel();

    @GsonBuilder
    public static Builder builder() {
        return new AutoValue_Results.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setChannel(Channel channel);
        public abstract Results build();
    }
}
