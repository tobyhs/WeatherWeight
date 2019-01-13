package io.github.tobyhs.weatherweight.data.model;

import java.util.Collections;

import org.junit.Test;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZonedDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ForecastResultSetTest {
    @Test
    public void testBuilder() {
        DailyForecast dailyForecast = DailyForecast.builder()
                .setDate(LocalDate.parse("2018-01-13"))
                .setLow(60)
                .setHigh(70)
                .setText("Cloudy")
                .build();
        ForecastResultSet forecast = ForecastResultSet.builder()
                .setLocation("San Francisco, CA, USA")
                .setPublicationTime(ZonedDateTime.parse("2019-01-13T16:00:00-08:00"))
                .setForecasts(Collections.singletonList(dailyForecast))
                .build();

        assertThat(forecast.getLocation(), is("San Francisco, CA, USA"));
        assertThat(forecast.getPublicationTime().toString(), is("2019-01-13T16:00-08:00"));
        assertThat(forecast.getForecasts(), is(Collections.singletonList(dailyForecast)));
    }
}
