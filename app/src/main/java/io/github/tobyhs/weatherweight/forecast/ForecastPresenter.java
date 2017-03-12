package io.github.tobyhs.weatherweight.forecast;

import javax.inject.Inject;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import io.reactivex.observers.DisposableSingleObserver;

import io.github.tobyhs.weatherweight.util.SchedulerProvider;
import io.github.tobyhs.weatherweight.yahooweather.WeatherRepository;
import io.github.tobyhs.weatherweight.yahooweather.model.Channel;

/**
 * Presenter for the forecast activity
 */
public class ForecastPresenter extends MvpBasePresenter<ForecastContract.View> {
    private Channel channel;
    private final SchedulerProvider schedulerProvider;
    private final WeatherRepository weatherRepository;

    /**
     * @param schedulerProvider RX scheduler provider
     * @param weatherRepository weather repository object to obtain forecast data
     */
    @Inject
    public ForecastPresenter(SchedulerProvider schedulerProvider, WeatherRepository weatherRepository) {
        this.schedulerProvider = schedulerProvider;
        this.weatherRepository = weatherRepository;
    }

    /**
     * Obtains forecast data for the given location
     *
     * @param location location to obtain forecast for
     */
    public void search(String location) {
        channel = null;
        getView().showLoading(false);

        weatherRepository.getForecast(location)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(new DisposableSingleObserver<Channel>() {
                    @Override
                    public void onSuccess(Channel channel) {
                        setChannel(channel);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().showError(e, false);
                        }
                    }
                });
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
    private void setChannel(Channel channel) {
        this.channel = channel;
        if (isViewAttached()) {
            getView().setData(channel);
            getView().showContent();
        }
    }
}
