package io.github.tobyhs.weatherweight.data.model;

import java.util.List;

import com.google.auto.value.AutoValue;
import me.tatarka.gsonvalue.annotations.GsonBuilder;
import org.threeten.bp.ZonedDateTime;

/**
 * A result set for weather forecasts
 */
@AutoValue
public abstract class ForecastResultSet {
    /**
     * @return location this forecast is for
     */
    public abstract String getLocation();

    /**
     * @return publication time of this forecast
     */
    public abstract ZonedDateTime getPublicationTime();

    /**
     * @return daily forecasts for today and upcoming days
     */
    public abstract List<DailyForecast> getForecasts();

    @GsonBuilder
    public static Builder builder() {
        return new AutoValue_ForecastResultSet.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setLocation(String location);
        public abstract Builder setPublicationTime(ZonedDateTime publicationTime);
        public abstract Builder setForecasts(List<DailyForecast> forecasts);
        public abstract ForecastResultSet build();
    }
}
