package io.github.tobyhs.weatherweight.yahooweather.model;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Location for a forecast
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class Location {
    private String city;
    private String region;
    private String country;

    /**
     * @return city of this location
     */
    public String getCity() {
        return city;
    }

    /**
     * @return region/state/province of this location
     */
    public String getRegion() {
        return region;
    }

    /**
     * @return country of this location
     */
    public String getCountry() {
        return country;
    }

    public Location setCity(String city) {
        this.city = city;
        return this;
    }

    public Location setRegion(String region) {
        this.region = region;
        return this;
    }

    public Location setCountry(String country) {
        this.country = country;
        return this;
    }

    @Override
    public String toString() {
        return city + ", " + region + ", " + country;
    }
}
