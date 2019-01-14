package io.github.tobyhs.weatherweight.api.accuweather.locations;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import me.tatarka.gsonvalue.annotations.GsonBuilder;

/**
 * A country from AccuWeather's Locations API
 */
@AutoValue
public abstract class Country {
    /**
     *
     * @return ISO code for country
     */
    @SerializedName("ID")
    public abstract String getId();

    @GsonBuilder
    public static Builder builder() {
        return new AutoValue_Country.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setId(String id);
        public abstract Country build();
    }
}
