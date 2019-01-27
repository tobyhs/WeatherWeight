package io.github.tobyhs.weatherweight.data;

/**
 * This exception is thrown when a {@code WeatherRepository} can't find a given location.
 */
public class LocationNotFoundError extends Exception {
    /**
     * @param location the location that was not found
     */
    public LocationNotFoundError(String location) {
        super("Location " + location + " not found");
    }
}
