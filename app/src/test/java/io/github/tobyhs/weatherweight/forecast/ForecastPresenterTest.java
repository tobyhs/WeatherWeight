package io.github.tobyhs.weatherweight.forecast;

import java.util.concurrent.atomic.AtomicBoolean;

import com.github.tobyhs.rxsecretary.SchedulerProvider;
import com.github.tobyhs.rxsecretary.TrampolineSchedulerProvider;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import io.github.tobyhs.weatherweight.storage.LastForecastStore;
import io.github.tobyhs.weatherweight.test.BaseTestCase;
import io.github.tobyhs.weatherweight.test.WeatherResponseFactory;
import io.github.tobyhs.weatherweight.yahooweather.LocationNotFoundError;
import io.github.tobyhs.weatherweight.yahooweather.WeatherRepository;
import io.github.tobyhs.weatherweight.yahooweather.model.Channel;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ForecastPresenterTest extends BaseTestCase {
    private SchedulerProvider schedulerProvider = new TrampolineSchedulerProvider();
    @Mock private WeatherRepository weatherRepository;
    @Mock private LastForecastStore lastForecastStore;
    @Mock private ForecastContract.View view;
    private ForecastPresenter presenter;

    @Before
    public void setup() {
        presenter = new ForecastPresenter(schedulerProvider, weatherRepository, lastForecastStore);
        presenter.attachView(view);
    }

    @Test
    public void loadLastForecastOnSuccess() {
        Channel channel = WeatherResponseFactory.createChannel();
        when(lastForecastStore.get()).thenReturn(Maybe.just(channel));

        presenter.loadLastForecast();
        checkChannelSet(channel);
        verify(view).setLocationInputText(channel.getLocation().toString());
    }

    @Test
    public void loadLastForecastOnError() {
        Throwable error = new Exception("testing");
        when(lastForecastStore.get()).thenReturn(Maybe.error(error));

        presenter.loadLastForecast();
        verify(view).showError(error, false);
    }

    @Test
    public void loadLastForecastOnComplete() {
        when(lastForecastStore.get()).thenReturn(Maybe.empty());

        presenter.loadLastForecast();
        verify(view).showContent();
    }

    @Test
    public void searchOnSuccess() {
        String location = "San Francisco, CA";
        Channel channel = WeatherResponseFactory.createChannel();
        when(weatherRepository.getForecast(location)).thenReturn(Single.just(channel));

        final AtomicBoolean completableSubscribed = new AtomicBoolean(false);
        Completable saveCompletable = Completable.complete()
                .doOnSubscribe(disposable -> completableSubscribed.set(true));
        when(lastForecastStore.save(channel)).thenReturn(saveCompletable);

        presenter.search(location);

        verify(view).showLoading(false);
        checkChannelSet(channel);
        assertThat(completableSubscribed.get(), is(true));
        assertThat(presenter.getAttributionUrl(), is(channel.getLink()));
    }

    @Test
    public void searchOnError() {
        String location = "Parts Unknown";
        LocationNotFoundError locationError = new LocationNotFoundError(location);
        Single<Channel> single = Single.error(locationError);
        when(weatherRepository.getForecast(location)).thenReturn(single);

        presenter.search(location);

        verify(view).showLoading(false);
        assertThat(presenter.getChannel(), is(nullValue()));
        verify(view).showError(locationError, false);
    }

    @Test
    public void getAttributionUrl() {
        assertThat(presenter.getAttributionUrl(), is(WeatherRepository.ATTRIBUTION_URL));
    }

    /**
     * Checks that the given channel was set on the presenter
     *
     * @param channel {@link Channel} to check against
     */
    private void checkChannelSet(Channel channel) {
        assertThat(presenter.getChannel(), is(channel));
        verify(view).setData(channel);
        verify(view).showContent();
    }
}
