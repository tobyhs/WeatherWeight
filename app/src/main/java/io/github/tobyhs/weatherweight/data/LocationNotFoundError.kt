package io.github.tobyhs.weatherweight.data

/**
 * This exception is thrown when a `WeatherRepository` can't find a given location.
 */
class LocationNotFoundError
/**
 * @param location the location that was not found
 */
    (location: String) : Exception("Location $location not found")
