package io.github.tobyhs.weatherweight.test;

import java.util.Arrays;
import java.util.List;

import org.threeten.bp.LocalDate;
import org.threeten.bp.ZonedDateTime;

import io.github.tobyhs.weatherweight.data.model.DailyForecast;
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet;

/**
 * This creates {@link ForecastResultSet} objects for tests
 */
public class ForecastResultSetFactory {
    /**
     * @return a valid {@link ForecastResultSet} for testing
     */
    public static ForecastResultSet create() {
        return ForecastResultSet.builder()
                .setLocation("Oakland, CA, US")
                .setPublicationTime(ZonedDateTime.parse("2019-02-01T12:00:00Z"))
                .setForecasts(createForecasts())
                .build();
    }

    /**
     * @return a list of daily forecasts for testing
     */
    public static List<DailyForecast> createForecasts() {
        return Arrays.asList(
                DailyForecast.builder().setDate(LocalDate.parse("2019-02-01"))
                        .setLow(60).setHigh(65).setText("Cloudy").build(),
                DailyForecast.builder().setDate(LocalDate.parse("2019-02-02"))
                        .setLow(58).setHigh(64).setText("Showers").build()
        );
    }
}
