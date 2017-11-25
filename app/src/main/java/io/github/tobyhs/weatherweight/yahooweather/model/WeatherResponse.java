package io.github.tobyhs.weatherweight.yahooweather.model;

import com.google.auto.value.AutoValue;

import me.tatarka.gsonvalue.annotations.GsonBuilder;

/**
 * This represents a response from querying the "weather.forecast" table
 */
@AutoValue
public abstract class WeatherResponse {
    /**
     * @return resulting metadata and data for the sent query
     */
    public abstract Query getQuery();

    @GsonBuilder
    public static Builder builder() {
        return new AutoValue_WeatherResponse.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setQuery(Query query);
        public abstract WeatherResponse build();
    }
}
