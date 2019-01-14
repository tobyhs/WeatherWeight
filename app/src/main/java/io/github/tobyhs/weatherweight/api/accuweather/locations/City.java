package io.github.tobyhs.weatherweight.api.accuweather.locations;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import me.tatarka.gsonvalue.annotations.GsonBuilder;

/**
 * A city from AccuWeather's Locations API
 */
@AutoValue
public abstract class City {
    /**
     * @return location key
     */
    @SerializedName("Key")
    public abstract String getKey();

    /**
     * @return name of this city
     */
    @SerializedName("LocalizedName")
    public abstract String getLocalizedName();

    /**
     * @return country of this city
     */
    @SerializedName("Country")
    public abstract Country getCountry();

    /**
     * @return administrative area of this city
     */
    @SerializedName("AdministrativeArea")
    public abstract AdministrativeArea getAdministrativeArea();

    @GsonBuilder
    public static Builder builder() {
        return new AutoValue_City.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setKey(String key);
        public abstract Builder setLocalizedName(String localizedName);
        public abstract Builder setCountry(Country country);
        public abstract Builder setAdministrativeArea(AdministrativeArea administrativeArea);
        public abstract City build();
    }
}
