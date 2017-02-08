package io.github.tobyhs.weatherweight.yahooweather;

import io.github.tobyhs.weatherweight.yahooweather.model.Results;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import retrofit2.Response;

import io.github.tobyhs.weatherweight.yahooweather.model.Channel;
import io.github.tobyhs.weatherweight.yahooweather.model.WeatherResponse;

/**
 * Implementation of {@link WeatherRepository} that uses {@link WeatherService}
 */
public class WeatherRepositoryImpl implements WeatherRepository {
    private static final String GET_FORECAST_FORMAT_STR =
            "SELECT * FROM weather.forecast WHERE woeid IN " +
                    "(SELECT woeid FROM geo.places(1) WHERE text='%s')";

    private final WeatherService weatherService;

    /**
     * @param weatherService service to fetch weather data from
     */
    public WeatherRepositoryImpl(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Override
    public Single<Channel> getForecast(final String location) {
        // TODO escape location
        String yql = String.format(GET_FORECAST_FORMAT_STR, location);
        Single<Response<WeatherResponse>> single = weatherService.getByYql(yql);
        return single.map(new Function<Response<WeatherResponse>, Channel>() {
            @Override
            public Channel apply(Response<WeatherResponse> response) throws Exception {
                Results results = response.body().getQuery().getResults();
                if (results == null) {
                    throw new LocationNotFoundError(location);
                }
                return results.getChannel();
            }
        });
    }
}
