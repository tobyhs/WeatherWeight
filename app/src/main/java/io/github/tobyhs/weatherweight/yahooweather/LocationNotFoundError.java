package io.github.tobyhs.weatherweight.yahooweather;

/**
 * This exception is thrown when {@code WeatherService} can't find a given location.
 */
public class LocationNotFoundError extends Exception {
    public LocationNotFoundError(String location) {
        super("Location " + location + " not found");
    }
}
