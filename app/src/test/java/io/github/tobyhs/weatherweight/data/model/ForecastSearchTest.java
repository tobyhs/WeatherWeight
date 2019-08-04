package io.github.tobyhs.weatherweight.data.model;

import org.junit.Test;

import io.github.tobyhs.weatherweight.test.ForecastResultSetFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ForecastSearchTest {
    @Test
    public void testBuilder() {
        ForecastResultSet forecastResultSet =  ForecastResultSetFactory.create();
        String input = "Input City";
        ForecastSearch forecastSearch = ForecastSearch.builder()
                .setInput(input)
                .setForecastResultSet(forecastResultSet)
                .build();

        assertThat(forecastSearch.getInput(), is(input));
        assertThat(forecastSearch.getForecastResultSet(), is(forecastResultSet));
    }
}
