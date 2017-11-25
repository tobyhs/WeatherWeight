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
        Results results = Results.builder().setChannel(createChannel()).build();
        Query query = Query.builder().setResults(results).build();
        return WeatherResponse.builder().setQuery(query).build();
    }

    /**
     * @return a valid {@link Channel} for testing
     */
    public static Channel createChannel() {
        List<SingleForecast> forecast = Arrays.asList(
                SingleForecast.builder().setDate("05 Feb 2017").setDay("Sun")
                        .setLow("50").setHigh("55").setText("Showers").build(),
                SingleForecast.builder().setDate("06 Feb 2017").setDay("Mon")
                        .setLow("60").setHigh("63").setText("Cloudy").build(),
                SingleForecast.builder().setDate("07 Feb 2017").setDay("Tue")
                        .setLow("72").setHigh("76").setText("Sunny").build()
        );

        Location location = Location.builder()
                .setCity("Oakland").setRegion("CA").setCountry("United States").build();
        Item item = Item.builder()
                .setForecast(forecast)
                .setPubDate("Sun, 05 Feb 2017 08:00 PM PST")
                .build();
        String link = "http://us.rd.yahoo.com/dailynews/rss/weather/Country__Country/*https://weather.yahoo.com/country/state/city-2463583/";
        return Channel.builder().setLocation(location).setItem(item).setLink(link).build();
    }
}
