package io.github.tobyhs.weatherweight.data;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import org.threeten.bp.Clock;
import org.threeten.bp.ZonedDateTime;

import io.github.tobyhs.weatherweight.api.accuweather.AccuWeatherService;
import io.github.tobyhs.weatherweight.api.accuweather.locations.City;
import io.github.tobyhs.weatherweight.data.model.DailyForecast;
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet;

/**
 * Implementation of {@link WeatherRepository} that uses AccuWeather's APIs
 */
public class AccuWeatherRepository implements WeatherRepository {
    private final AccuWeatherService accuWeatherService;
    private final Clock clock;

    /**
     * @param accuWeatherService service to get data from AccuWeather's APIs
     * @param clock clock used to get the current instant
     */
    public AccuWeatherRepository(AccuWeatherService accuWeatherService, Clock clock) {
        this.accuWeatherService = accuWeatherService;
        this.clock = clock;
    }

    @Override
    public Single<ForecastResultSet> getForecast(String location) {
        final ForecastResultSet.Builder resultBuilder = ForecastResultSet.builder();
        // An explicit offset of 0 limits the results to 25 instead of 100
        return accuWeatherService.searchCities(location, 0).flatMap(citiesResponse -> {
            City city;
            try {
                city = citiesResponse.body().get(0);
            } catch (IndexOutOfBoundsException e) {
                throw new LocationNotFoundError(location);
            }
            resultBuilder.setLocation(formatCity(city));
            return accuWeatherService.get5DayForecast(city.getKey());
        }).map(response ->
            resultBuilder.setPublicationTime(ZonedDateTime.now(clock))
                    .setForecasts(convertDailyForecastResponse(response.body().getDailyForecasts()))
                    .build()
        );
    }

    /**
     * Derives a location string from a city from AccuWeather's API
     *
     * @param city a city from AccuWeather's Locations API
     * @return a location string with the city name, state/province, and country
     */
    private String formatCity(City city) {
        return city.getLocalizedName() + ", " + city.getAdministrativeArea().getId() +
                ", " + city.getCountry().getId();
    }

    /**
     * Converts daily forecasts from AccuWeather's Forecasts API
     *
     * @param dailyForecasts daily forecasts from AccuWeather's Forecasts API
     * @return converted daily forecasts
     */
    private List<DailyForecast> convertDailyForecastResponse(
            List<io.github.tobyhs.weatherweight.api.accuweather.forecasts.DailyForecast> dailyForecasts
    ) {
        ArrayList<DailyForecast> result = new ArrayList<>(dailyForecasts.size());
        for (io.github.tobyhs.weatherweight.api.accuweather.forecasts.DailyForecast forecast : dailyForecasts) {
            String text = forecast.getDay().getIconPhrase();
            String nightPhrase = forecast.getNight().getIconPhrase();
            if (!text.equals(nightPhrase)) {
                text += " / " + nightPhrase;
            }

            result.add(DailyForecast.builder()
                    .setDate(forecast.getDate().toLocalDate())
                    .setLow((int) forecast.getTemperature().getMinimum().getValue())
                    .setHigh((int) forecast.getTemperature().getMaximum().getValue())
                    .setText(text)
                    .build());
        }
        return result;
    }
}
