package io.github.tobyhs.weatherweight.yahooweather.model;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Resulting metadata and data from a Yahoo Weather API query
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class Query {
    private Results results;

    /**
     * @return resulting data
     */
    public Results getResults() {
        return results;
    }

    public Query setResults(Results results) {
        this.results = results;
        return this;
    }
}
