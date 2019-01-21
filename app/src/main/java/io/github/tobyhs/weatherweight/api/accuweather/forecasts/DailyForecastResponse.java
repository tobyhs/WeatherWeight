package io.github.tobyhs.weatherweight.api.accuweather.forecasts;

import java.util.List;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import me.tatarka.gsonvalue.annotations.GsonBuilder;

/**
 * A response from one of AccuWeather's Forecasts API endpoints
 */
@AutoValue
public abstract class DailyForecastResponse {
    /**
     * @return list of daily forecasts
     */
    @SerializedName("DailyForecasts")
    public abstract List<DailyForecast> getDailyForecasts();

    @GsonBuilder
    public static Builder builder() {
        return new AutoValue_DailyForecastResponse.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder setDailyForecasts(List<DailyForecast> dailyForecasts);
        public abstract DailyForecastResponse build();
    }
}
