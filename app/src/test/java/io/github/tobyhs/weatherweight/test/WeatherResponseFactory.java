package io.github.tobyhs.weatherweight.test;

import java.util.Arrays;
import java.util.List;

import io.github.tobyhs.weatherweight.yahooweather.model.Channel;
import io.github.tobyhs.weatherweight.yahooweather.model.Item;
import io.github.tobyhs.weatherweight.yahooweather.model.Location;
import io.github.tobyhs.weatherweight.yahooweather.model.Query;
import io.github.tobyhs.weatherweight.yahooweather.model.Results;
import io.github.tobyhs.weatherweight.yahooweather.model.SingleForecast;
import io.github.tobyhs.weatherweight.yahooweather.model.WeatherResponse;

/**
 * @see .create()
 */
public class WeatherResponseFactory {
    /**
     * This creates a valid {@link WeatherResponse} object for testing.
     *
     * @return a valid {@link WeatherResponse} for testing
     */
    public static WeatherResponse create() {
        List<SingleForecast> forecast = Arrays.asList(
                new SingleForecast().setDate("05 Feb 2017")
                        .setLow("50").setHigh("55").setText("Showers"),
                new SingleForecast().setDate("06 Feb 2017")
                        .setLow("60").setHigh("63").setText("Cloudy"),
                new SingleForecast().setDate("07 Feb 2017")
                        .setLow("72").setHigh("76").setText("Sunny")
        );

        Location location = new Location()
                .setCity("Oakland").setRegion("CA").setCountry("United States");
        Item item = new Item().setForecast(forecast);
        Channel channel = new Channel()
                .setLocation(location)
                .setItem(item)
                .setLink("http://us.rd.yahoo.com/dailynews/rss/weather/Country__Country/*https://weather.yahoo.com/country/state/city-2463583/");
        Results results = new Results().setChannel(channel);

        return new WeatherResponse().setQuery(new Query().setResults(results));
    }
}
