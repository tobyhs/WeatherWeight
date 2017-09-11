package io.github.tobyhs.weatherweight.yahooweather.model;

import java.io.InputStream;

import com.bluelinelabs.logansquare.LoganSquare;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class WeatherResponseTest {
    @Test
    public void parseJson() throws Exception {
        try (InputStream stream = getClass().getResourceAsStream("/weatherResponse.json")) {
            WeatherResponse response = LoganSquare.parse(stream, WeatherResponse.class);
            Channel channel = response.getQuery().getResults().getChannel();

            Location location = channel.getLocation();
            assertThat(location.getCity(), is("San Francisco"));
            assertThat(location.getRegion(), is(" CA"));
            assertThat(location.getCountry(), is("United States"));

            Item item = channel.getItem();
            assertThat(item.getPubDate(), is("Sun, 10 Sep 2017 01:00 PM PDT"));
            SingleForecast forecast = item.getForecast().get(0);
            assertThat(forecast.getDate(), is("10 Sep 2017"));
            assertThat(forecast.getDay(), is("Sun"));
            assertThat(forecast.getHigh(), is("84"));
            assertThat(forecast.getLow(), is("59"));
            assertThat(forecast.getText(), is("Sunny"));

            assertThat(channel.getLink(), is("http://us.rd.yahoo.com/dailynews/rss/weather/Country__Country/*https://weather.yahoo.com/country/state/city-2487956/"));
        }
    }
}
