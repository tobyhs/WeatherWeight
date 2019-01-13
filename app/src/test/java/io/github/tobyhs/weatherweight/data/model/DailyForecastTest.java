package io.github.tobyhs.weatherweight.data.model;

import org.junit.Test;
import org.threeten.bp.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DailyForecastTest {
    @Test
    public void testBuilder() {
        DailyForecast dailyForecast = DailyForecast.builder()
                .setDate(LocalDate.parse("2018-01-11"))
                .setLow(65)
                .setHigh(75)
                .setText("Showers")
                .build();

        assertThat(dailyForecast.getDate().toString(), is("2018-01-11"));
        assertThat(dailyForecast.getLow(), is(65));
        assertThat(dailyForecast.getHigh(), is(75));
        assertThat(dailyForecast.getText(), is("Showers"));
    }
}
