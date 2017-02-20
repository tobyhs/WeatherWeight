package io.github.tobyhs.weatherweight.yahooweather.model;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Weather data for one location
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class Channel {
    private Location location;
    private Item item;
    private String link;

    /**
     * @return location the forecast is for
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @return entry that contains conditions and forecasts
     */
    public Item getItem() {
        return item;
    }

    /**
     * @return link to corresponding Yahoo weather page for the location
     */
    public String getLink() {
        return link;
    }

    public Channel setLocation(Location location) {
        this.location = location;
        return this;
    }

    public Channel setItem(Item item) {
        this.item = item;
        return this;
    }

    public Channel setLink(String link) {
        this.link = link;
        return this;
    }
}
