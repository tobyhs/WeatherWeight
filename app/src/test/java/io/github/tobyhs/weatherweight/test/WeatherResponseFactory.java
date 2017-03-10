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
 * This creates {@link WeatherResponse} and related objects for testing.
 */
public class WeatherResponseFactory {
    /**
     * @return a valid {@link WeatherResponse} for testing
     */
    public static WeatherResponse create() {
        Results results = new Results().setChannel(createChannel());
        return new WeatherResponse().setQuery(new Query().setResults(results));
    }

    /**
     * @return a valid {@link Channel} for testing
     */
    public static Channel createChannel() {
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
        Item item = new Item().setForecast(forecast).setPubDate("Sun, 05 Feb 2017 08:00 PM PST");
        String link = "http://us.rd.yahoo.com/dailynews/rss/weather/Country__Country/*https://weather.yahoo.com/country/state/city-2463583/";
        return new Channel()
                .setLocation(location)
                .setItem(item)
                .setLink(link);
    }
}
