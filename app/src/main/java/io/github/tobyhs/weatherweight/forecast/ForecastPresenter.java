package io.github.tobyhs.weatherweight.forecast;

import javax.inject.Inject;

import com.github.tobyhs.rxsecretary.SchedulerProvider;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;

import io.github.tobyhs.weatherweight.storage.LastForecastStore;
import io.github.tobyhs.weatherweight.yahooweather.WeatherRepository;
import io.github.tobyhs.weatherweight.yahooweather.model.Channel;

/**
 * Presenter for the forecast activity
 */
public class ForecastPresenter extends MvpBasePresenter<ForecastContract.View> {
    private Channel channel;
    private final SchedulerProvider schedulerProvider;
    private final WeatherRepository weatherRepository;
    private final LastForecastStore lastForecastStore;

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
     * Loads the last forecast ({@link Channel}) that was retrieved when the activity first starts.
     */
    public void loadLastForecast() {
        lastForecastStore.get()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(channel -> {
                    setChannel(channel);
                    ifViewAttached(view -> {
                        String location = channel.getLocation().toString();
                        view.setLocationInputText(location);
                    });
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
        this.channel = null;
        ifViewAttached(view -> view.showLoading(false));

        weatherRepository.getForecast(location)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(channel -> {
                    setChannel(channel);
                    lastForecastStore.save(channel).subscribeOn(schedulerProvider.io()).subscribe();
                }, error -> ifViewAttached(view -> view.showError(error, false)));
    }

    /**
     * @return object with the forecast data we are presenting
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * @return attribution URL to link to
     */
    public String getAttributionUrl() {
        if (channel == null) {
            return WeatherRepository.ATTRIBUTION_URL;
        } else {
            return channel.getLink();
        }
    }

    /**
     * Displays the given forecast data
     *
     * @param channel object with forecast data to set and display
     */
    private void setChannel(final Channel channel) {
        this.channel = channel;
        ifViewAttached(view -> {
            view.setData(channel);
            view.showContent();
        });
    }
}
