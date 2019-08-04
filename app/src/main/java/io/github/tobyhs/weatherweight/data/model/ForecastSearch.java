package io.github.tobyhs.weatherweight.data.model;

import com.google.auto.value.AutoValue;
import me.tatarka.gsonvalue.annotations.GsonBuilder;

/**
 * This contains the input location and the resulting forecast data for a forecast search/query.
 */
@AutoValue
public abstract class ForecastSearch {
    /**
     * @return the input location entered for the search
     */
    public abstract String getInput();

    /**
     * @return the forecast result of the search
     */
    public abstract ForecastResultSet getForecastResultSet();

    @GsonBuilder
    public static Builder builder() {
        return new AutoValue_ForecastSearch.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setInput(String input);
        public abstract Builder setForecastResultSet(ForecastResultSet forecastResultSet);
        public abstract ForecastSearch build();
    }
}
