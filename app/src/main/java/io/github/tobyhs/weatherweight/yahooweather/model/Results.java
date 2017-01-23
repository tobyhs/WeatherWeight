package io.github.tobyhs.weatherweight.yahooweather.model;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Resulting data from a Yahoo Weather API query
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class Results {
    private Channel channel;

    /**
     * @return weather data for one location
     */
    public Channel getChannel() {
        return channel;
    }

    public Results setChannel(Channel channel) {
        this.channel = channel;
        return this;
    }
}
