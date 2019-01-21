package io.github.tobyhs.weatherweight.api.accuweather.forecasts;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import com.google.gson.Gson;
import org.junit.Test;
import org.threeten.bp.format.DateTimeFormatter;

import io.github.tobyhs.weatherweight.AppModule;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DailyForecastResponseTest {
    @Test
    public void parseJson() throws Exception {
        try (
                InputStream stream = getClass().getResourceAsStream("/accuweather/dailyForecastResponse.json");
                Reader reader = new InputStreamReader(stream)
        ) {
            Gson gson = AppModule.provideGson();
            DailyForecastResponse response = gson.fromJson(reader, DailyForecastResponse.class);

            List<DailyForecast> dailyForecasts =  response.getDailyForecasts();
            assertThat(dailyForecasts.size(), is(1));

            DailyForecast dailyForecast = dailyForecasts.get(0);
            assertThat(dailyForecast.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE), is("2019-01-11"));

            assertThat(dailyForecast.getTemperature().getMinimum().getValue(), is(52.0));
            assertThat(dailyForecast.getTemperature().getMaximum().getValue(), is(57.0));

            assertThat(dailyForecast.getDay().getIconPhrase(), is("Showers"));
            assertThat(dailyForecast.getNight().getIconPhrase(), is("Rain"));
        }
    }
}
