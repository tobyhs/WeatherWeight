package io.github.tobyhs.weatherweight.test;

import io.github.tobyhs.weatherweight.data.model.ForecastSearch;

/**
 * This contains method(s) for creating {@link ForecastSearch}s for tests
 */
public class ForecastSearchFactory {
    /**
     * @return a valid {@link ForecastSearch} for tests
     */
    public static ForecastSearch create() {
        return ForecastSearch.builder()
                .setInput("Oakland, CA")
                .setForecastResultSet(ForecastResultSetFactory.create())
                .build();
    }
}
