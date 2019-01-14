package io.github.tobyhs.weatherweight.api.accuweather.locations;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import me.tatarka.gsonvalue.annotations.GsonBuilder;

/**
 * An administrative area (state, province, etc.) from AccuWeather's Locations API
 */
@AutoValue
public abstract class AdministrativeArea {
    /**
     * @return ID for this admin area
     */
    @SerializedName("ID")
    public abstract String getId();

    @GsonBuilder
    public static Builder builder() {
        return new AutoValue_AdministrativeArea.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setId(String id);
        public abstract AdministrativeArea build();
    }
}
