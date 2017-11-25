package io.github.tobyhs.weatherweight.yahooweather.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import me.tatarka.gsonvalue.annotations.GsonBuilder;

/**
 * Resulting metadata and data from a Yahoo Weather API query
 */
@AutoValue
public abstract class Query {
    /**
     * @return resulting data
     */
    @Nullable
    public abstract Results getResults();

    @GsonBuilder
    public static Builder builder() {
        return new AutoValue_Query.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setResults(Results results);
        public abstract Query build();
    }
}
