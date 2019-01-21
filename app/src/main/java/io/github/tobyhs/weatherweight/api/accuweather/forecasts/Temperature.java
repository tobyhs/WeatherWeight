package io.github.tobyhs.weatherweight.api.accuweather.forecasts;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import me.tatarka.gsonvalue.annotations.GsonBuilder;

/**
 * A temperature from AccuWeather's APIs
 */
@AutoValue
public abstract class Temperature {
    /**
     * @return the value of the temperature
     */
    @SerializedName("Value")
    public abstract double getValue();

    @GsonBuilder
    public static Builder builder() {
        return new AutoValue_Temperature.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder setValue(double value);
        public abstract Temperature build();
    }
}
