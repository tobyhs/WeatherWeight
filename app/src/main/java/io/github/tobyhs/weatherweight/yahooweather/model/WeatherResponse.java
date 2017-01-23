package io.github.tobyhs.weatherweight.yahooweather.model;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * This represents a response from querying the "weather.forecast" table
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class WeatherResponse {
    private Query query;

    /**
     * @return resulting metadata and data for the sent query
     */
    public Query getQuery() {
        return query;
    }

    public WeatherResponse setQuery(Query query) {
        this.query = query;
        return this;
    }
}
