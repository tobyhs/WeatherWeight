package io.github.tobyhs.weatherweight.api.accuweather.forecasts;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import me.tatarka.gsonvalue.annotations.GsonBuilder;

/**
 * A range of temperatures for a daily forecast
 */
@AutoValue
public abstract class TemperatureRange {
    /**
     * @return the forecasted minimum temperature for a day
     */
    @SerializedName("Minimum")
    public abstract Temperature getMinimum();

    /**
     * @return the forecasted maximum temperature for a day
     */
    @SerializedName("Maximum")
    public abstract Temperature getMaximum();

    @GsonBuilder
    public static Builder builder() {
        return new AutoValue_TemperatureRange.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder setMinimum(Temperature minimum);
        public abstract Builder setMaximum(Temperature maximum);
        public abstract TemperatureRange build();
    }
}
