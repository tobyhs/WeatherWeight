package io.github.tobyhs.weatherweight.yahooweather.model;

import com.google.auto.value.AutoValue;

import me.tatarka.gsonvalue.annotations.GsonBuilder;

/**
 * Location for a forecast
 */
@AutoValue
public abstract class Location {
    /**
     * @return city of this location
     */
    public abstract String getCity();

    /**
     * @return region/state/province of this location
     */
    public abstract String getRegion();

    /**
     * @return country of this location
     */
    public abstract String getCountry();

    @Override
    public String toString() {
        return getCity() + ", " + getRegion() + ", " + getCountry();
    }

    @GsonBuilder
    public static Builder builder() {
        return new AutoValue_Location.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setCity(String city);
        public abstract Builder setRegion(String region);
        public abstract Builder setCountry(String country);
        public abstract Location build();
    }
}
