package io.github.tobyhs.weatherweight.forecast;

import java.util.concurrent.atomic.AtomicBoolean;

import com.github.tobyhs.rxsecretary.SchedulerProvider;
import com.github.tobyhs.rxsecretary.TrampolineSchedulerProvider;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;

import io.github.tobyhs.weatherweight.data.LocationNotFoundError;
import io.github.tobyhs.weatherweight.data.model.ForecastSearch;
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet;
import io.github.tobyhs.weatherweight.data.WeatherRepository;
import io.github.tobyhs.weatherweight.storage.LastForecastStore;
import io.github.tobyhs.weatherweight.test.BaseTestCase;
import io.github.tobyhs.weatherweight.test.ForecastResultSetFactory;
import io.github.tobyhs.weatherweight.test.ForecastSearchFactory;

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
        ForecastSearch forecastSearch = ForecastSearchFactory.create();
        when(lastForecastStore.get()).thenReturn(Maybe.just(forecastSearch));

        presenter.loadLastForecast();
        checkForecastResultSet(forecastSearch.getForecastResultSet());
        verify(view).setLocationInputText(forecastSearch.getInput());
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
        String location = "Oakland, CA";
        ForecastResultSet forecastResultSet = ForecastResultSetFactory.create();
        when(weatherRepository.getForecast(location)).thenReturn(Single.just(forecastResultSet));

        final AtomicBoolean completableSubscribed = new AtomicBoolean(false);
        Completable saveCompletable = Completable.complete()
                .doOnSubscribe(disposable -> completableSubscribed.set(true));
        ForecastSearch forecastSearch = ForecastSearch.builder()
                .setInput(location)
                .setForecastResultSet(forecastResultSet)
                .build();
        when(lastForecastStore.save(forecastSearch)).thenReturn(saveCompletable);

        presenter.search(location);

        verify(view).showLoading(false);
        checkForecastResultSet(forecastResultSet);
        assertThat(completableSubscribed.get(), is(true));
    }

    @Test
    public void searchOnError() {
        String location = "Parts Unknown";
        LocationNotFoundError locationError = new LocationNotFoundError(location);
        Single<ForecastResultSet> single = Single.error(locationError);
        when(weatherRepository.getForecast(location)).thenReturn(single);

        presenter.search(location);

        verify(view).showLoading(false);
        assertThat(presenter.getForecastResultSet(), is(nullValue()));
        verify(view).showError(locationError, false);
    }

    /**
     * Checks that the given forecast result set was set on the presenter
     *
     * @param forecastResultSet {@link ForecastResultSet} to check against
     */
    private void checkForecastResultSet(ForecastResultSet forecastResultSet) {
        assertThat(presenter.getForecastResultSet(), is(forecastResultSet));
        verify(view).setData(forecastResultSet);
        verify(view).showContent();
    }
}
