package io.github.tobyhs.weatherweight.forecast;

import android.util.MutableBoolean;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import io.github.tobyhs.weatherweight.storage.LastForecastStore;
import io.github.tobyhs.weatherweight.test.BaseTestCase;
import io.github.tobyhs.weatherweight.test.TestSchedulerProvider;
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
    private TestSchedulerProvider schedulerProvider = new TestSchedulerProvider();
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
    public void searchOnSuccess() {
        String location = "San Francisco, CA";
        Channel channel = WeatherResponseFactory.createChannel();
        when(weatherRepository.getForecast(location)).thenReturn(Single.just(channel));

        final MutableBoolean completableSubscribed = new MutableBoolean(false);
        Completable saveCompletable = Completable.complete().doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                completableSubscribed.value = true;
            }
        });
        when(lastForecastStore.save(channel)).thenReturn(saveCompletable);

        presenter.search(location);
        verify(view).showLoading(false);

        schedulerProvider.triggerActions();
        assertThat(presenter.getChannel(), is(channel));
        verify(view).setData(channel);
        verify(view).showContent();

        schedulerProvider.triggerActions();
        assertThat(completableSubscribed.value, is(true));

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

        schedulerProvider.triggerActions();
        assertThat(presenter.getChannel(), is(nullValue()));
        verify(view).showError(locationError, false);
    }

    @Test
    public void getAttributionUrl() {
        assertThat(presenter.getAttributionUrl(), is(WeatherRepository.ATTRIBUTION_URL));
    }
}
