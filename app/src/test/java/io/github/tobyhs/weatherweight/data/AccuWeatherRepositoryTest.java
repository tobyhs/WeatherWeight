package io.github.tobyhs.weatherweight.data;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.observers.TestObserver;

import org.junit.Before;
import org.junit.Test;

import org.threeten.bp.Clock;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.ChronoUnit;

import retrofit2.Response;

import io.github.tobyhs.weatherweight.api.accuweather.AccuWeatherService;
import io.github.tobyhs.weatherweight.api.accuweather.forecasts.DailyForecast;
import io.github.tobyhs.weatherweight.api.accuweather.forecasts.DailyForecastResponse;
import io.github.tobyhs.weatherweight.api.accuweather.forecasts.DayPeriod;
import io.github.tobyhs.weatherweight.api.accuweather.forecasts.Temperature;
import io.github.tobyhs.weatherweight.api.accuweather.forecasts.TemperatureRange;
import io.github.tobyhs.weatherweight.api.accuweather.locations.AdministrativeArea;
import io.github.tobyhs.weatherweight.api.accuweather.locations.City;
import io.github.tobyhs.weatherweight.api.accuweather.locations.Country;
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccuWeatherRepositoryTest {
    private AccuWeatherService accuWeatherService;
    private final Clock clock = Clock.systemUTC();
    private AccuWeatherRepository repository;

    @Before
    public void setup() {
        accuWeatherService = mock(AccuWeatherService.class);
        repository = new AccuWeatherRepository(accuWeatherService, clock);
    }

    @Test
    public void getForecast_locationNotFound() {
        Response<List<City>> citiesResponse = Response.success(Collections.emptyList());
        Single<Response<List<City>>> citiesSingle = Single.just(citiesResponse);
        when(accuWeatherService.searchCities("Nowhere, USA", 0)).thenReturn(citiesSingle);

        TestObserver<ForecastResultSet> observer = repository.getForecast("Nowhere, USA").test();
        observer.assertError(LocationNotFoundError.class);
    }

    @Test
    public void getForecast() {
        List<City> cities = new LinkedList<>();
        cities.add(City.builder()
                .setKey("123456")
                .setLocalizedName("San Jose")
                .setAdministrativeArea(AdministrativeArea.builder().setId("CA").build())
                .setCountry(Country.builder().setId("US").build())
                .build());
        cities.add(City.builder()
                .setKey("432165")
                .setLocalizedName("San Jose")
                .setAdministrativeArea(AdministrativeArea.builder().setId("SJ").build())
                .setCountry(Country.builder().setId("CR").build())
                .build());
        Single<Response<List<City>>> citiesSingle = Single.just(Response.success(cities));
        when(accuWeatherService.searchCities("San Jose", 0)).thenReturn(citiesSingle);

        ZonedDateTime time = ZonedDateTime.parse("2018-01-29T12:00:00Z");
        List<DailyForecast> dailyForecasts = new LinkedList<>();
        TemperatureRange temperatureRange = TemperatureRange.builder()
                .setMinimum(Temperature.builder().setValue(50.0).build())
                .setMaximum(Temperature.builder().setValue(60.0).build())
                .build();
        dailyForecasts.add(DailyForecast.builder()
                .setDate(time)
                .setTemperature(temperatureRange)
                .setDay(DayPeriod.builder().setIconPhrase("Sunny").build())
                .setNight(DayPeriod.builder().setIconPhrase("Sunny").build())
                .build());
        temperatureRange = TemperatureRange.builder()
                .setMinimum(Temperature.builder().setValue(52.0).build())
                .setMaximum(Temperature.builder().setValue(62.0).build())
                .build();
        dailyForecasts.add(DailyForecast.builder()
                .setDate(time.plusDays(1))
                .setTemperature(temperatureRange)
                .setDay(DayPeriod.builder().setIconPhrase("Cloudy").build())
                .setNight(DayPeriod.builder().setIconPhrase("Showers").build())
                .build());
        DailyForecastResponse response = DailyForecastResponse.builder()
                .setDailyForecasts(dailyForecasts)
                .build();
        when(accuWeatherService.get5DayForecast("123456"))
                .thenReturn(Single.just(Response.success(response)));

        ForecastResultSet result = repository.getForecast("San Jose").blockingGet();
        assertThat(result.getLocation(), is("San Jose, CA, US"));
        assertThat(result.getPublicationTime().until(ZonedDateTime.now(clock), ChronoUnit.MINUTES), is(0L));

        assertThat(result.getForecasts().size(), is(2));
        io.github.tobyhs.weatherweight.data.model.DailyForecast dailyForecast;

        dailyForecast = result.getForecasts().get(0);
        assertThat(dailyForecast.getDate().toString(), is("2018-01-29"));
        assertThat(dailyForecast.getLow(), is(50));
        assertThat(dailyForecast.getHigh(), is(60));
        assertThat(dailyForecast.getText(), is("Sunny"));

        dailyForecast = result.getForecasts().get(1);
        assertThat(dailyForecast.getDate().toString(), is("2018-01-30"));
        assertThat(dailyForecast.getLow(), is(52));
        assertThat(dailyForecast.getHigh(), is(62));
        assertThat(dailyForecast.getText(), is("Cloudy / Showers"));
    }
}
