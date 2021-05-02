package io.github.tobyhs.weatherweight.forecast;

import androidx.annotation.VisibleForTesting;

import javax.inject.Inject;

import com.github.tobyhs.rxsecretary.SchedulerProvider;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;

import io.reactivex.rxjava3.disposables.Disposable;

import io.github.tobyhs.weatherweight.data.WeatherRepository;
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet;
import io.github.tobyhs.weatherweight.data.model.ForecastSearch;
import io.github.tobyhs.weatherweight.storage.LastForecastStore;

/**
 * Presenter for the forecast activity
 */
public class ForecastPresenter extends MvpBasePresenter<ForecastContract.View> {
    private final SchedulerProvider schedulerProvider;
    private final WeatherRepository weatherRepository;
    private final LastForecastStore lastForecastStore;

    @VisibleForTesting Disposable loadLastForecastDisposable;
    @VisibleForTesting Disposable getForecastDisposable;
    private ForecastResultSet forecastResultSet;

    /**
     * @param schedulerProvider RX scheduler provider
     * @param weatherRepository weather repository object to obtain forecast data
     * @param lastForecastStore store to save or get the last forecast
     */
    @Inject
    public ForecastPresenter(
            SchedulerProvider schedulerProvider,
            WeatherRepository weatherRepository,
            LastForecastStore lastForecastStore
    ) {
        this.schedulerProvider = schedulerProvider;
        this.weatherRepository = weatherRepository;
        this.lastForecastStore = lastForecastStore;
    }

    /**
     * Loads the last {@link ForecastSearch} that was saved when the activity first starts.
     */
    public void loadLastForecast() {
        loadLastForecastDisposable = lastForecastStore.get()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(forecastSearch -> {
                    setForecastResultSet(forecastSearch.getForecastResultSet());
                    ifViewAttached(view -> view.setLocationInputText(forecastSearch.getInput()));
                },
                        error -> ifViewAttached(view -> view.showError(error, false)),
                        () -> ifViewAttached(MvpLceView::showContent)
                );
    }

    /**
     * Obtains forecast data for the given location
     *
     * @param location location to obtain forecast for
     */
    public void search(String location) {
        if (getForecastDisposable != null) {
            getForecastDisposable.dispose();
        }
        this.forecastResultSet = null;
        ifViewAttached(view -> view.showLoading(false));

        getForecastDisposable = weatherRepository.getForecast(location)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(forecastResultSet -> {
                    setForecastResultSet(forecastResultSet);
                    ForecastSearch forecastSearch = ForecastSearch.builder()
                            .setInput(location)
                            .setForecastResultSet(forecastResultSet)
                            .build();
                    lastForecastStore.save(forecastSearch).subscribeOn(schedulerProvider.io()).subscribe();
                }, error -> ifViewAttached(view -> view.showError(error, false)));
    }

    /**
     * @return object with the forecast data we are presenting
     */
    public ForecastResultSet getForecastResultSet() {
        return forecastResultSet;
    }

    @Override
    public void destroy() {
        if (loadLastForecastDisposable != null) {
            loadLastForecastDisposable.dispose();
        }
        if (getForecastDisposable != null) {
            getForecastDisposable.dispose();
        }
        super.destroy();
    }

    /**
     * Displays the given forecast data
     *
     * @param forecastResultSet object with forecast data to set and display
     */
    private void setForecastResultSet(final ForecastResultSet forecastResultSet) {
        this.forecastResultSet = forecastResultSet;
        ifViewAttached(view -> {
            view.setData(forecastResultSet);
            view.showContent();
        });
    }
}
