package io.github.tobyhs.weatherweight.api.accuweather.forecasts;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import me.tatarka.gsonvalue.annotations.GsonBuilder;

/**
 * Forecast details about a period of the day (day or night)
 */
@AutoValue
public abstract class DayPeriod {
    /**
     * @return phrase description of the icon corresponding to the forecast
     */
    @SerializedName("IconPhrase")
    public abstract String getIconPhrase();

    @GsonBuilder
    public static Builder builder() {
        return new AutoValue_DayPeriod.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder setIconPhrase(String iconPhrase);
        public abstract DayPeriod build();
    }
}
