package io.github.tobyhs.weatherweight.yahooweather;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import retrofit2.Response;

import io.github.tobyhs.weatherweight.test.BaseTestCase;
import io.github.tobyhs.weatherweight.test.WeatherResponseFactory;
import io.github.tobyhs.weatherweight.yahooweather.model.Channel;
import io.github.tobyhs.weatherweight.yahooweather.model.Query;
import io.github.tobyhs.weatherweight.yahooweather.model.WeatherResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class WeatherRepositoryImplTest extends BaseTestCase {
    @Mock private WeatherService weatherService;
    @InjectMocks private WeatherRepositoryImpl weatherRepo;

    @Test
    public void getForecastWithNotFoundLocationThrowsError() {
        WeatherResponse weatherResponse = WeatherResponse.builder()
                .setQuery(Query.builder().build())
                .build();
        Response<WeatherResponse> response = Response.success(weatherResponse);
        when(weatherService.getByYql(anyString())).thenReturn(Single.just(response));

        TestObserver<Channel> observer = weatherRepo.getForecast("Unknown").test();
        observer.assertError(LocationNotFoundError.class);
    }

    @Test
    public void getForecastWithFoundLocationReturnsChannel() {
        WeatherResponse weatherResponse = WeatherResponseFactory.create();
        Response<WeatherResponse> response = Response.success(weatherResponse);
        String yql = "SELECT * FROM weather.forecast WHERE woeid IN " +
                "(SELECT woeid FROM geo.places(1) WHERE text='San Francisco, CA')";
        when(weatherService.getByYql(yql)).thenReturn(Single.just(response));

        Channel channel = weatherRepo.getForecast("San Francisco, CA").blockingGet();
        assertThat(channel, is(weatherResponse.getQuery().getResults().getChannel()));
    }
}
