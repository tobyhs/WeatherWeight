package io.github.tobyhs.weatherweight.forecast

import androidx.annotation.VisibleForTesting

import javax.inject.Inject

import com.github.tobyhs.rxsecretary.SchedulerProvider

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter

import io.github.tobyhs.weatherweight.data.WeatherRepository
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet
import io.github.tobyhs.weatherweight.data.model.ForecastSearch
import io.github.tobyhs.weatherweight.storage.LastForecastStore

import io.reactivex.rxjava3.disposables.Disposable

/**
 * Presenter for [ForecastActivity]
 */
class ForecastPresenter
/**
 * @param schedulerProvider RX scheduler provider
 * @param weatherRepository weather repository object to obtain forecast data
 * @param lastForecastStore store to save or get the last forecast
 */ @Inject constructor(
    private val schedulerProvider: SchedulerProvider,
    private val weatherRepository: WeatherRepository,
    private val lastForecastStore: LastForecastStore
) : MvpBasePresenter<ForecastContract.View>() {
    @VisibleForTesting
    var loadLastForecastDisposable: Disposable? = null

    @VisibleForTesting
    var getForecastDisposable: Disposable? = null

    /**
     * @return object with the forecast data we are presenting
     */
    var forecastResultSet: ForecastResultSet? = null
        internal set

    /**
     * Loads the last [ForecastSearch] that was saved when the activity first starts.
     */
    fun loadLastForecast() {
        loadLastForecastDisposable = lastForecastStore.get()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe(
                { forecastSearch ->
                    setForecastResultSet(forecastSearch.forecastResultSet)
                    ifViewAttached { it.setLocationInputText(forecastSearch.input) }
                },
                { error -> ifViewAttached { it.showError(error, false) } },
                { ifViewAttached { it.showContent() } }
            )
    }

    /**
     * Obtains forecast data for the given location
     *
     * @param location location to obtain forecast for
     */
    fun search(location: String) {
        getForecastDisposable?.dispose()
        forecastResultSet = null
        ifViewAttached { view -> view.showLoading(false) }

        getForecastDisposable = weatherRepository.getForecast(location)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe(
                { forecastResultSet ->
                    setForecastResultSet(forecastResultSet)
                    val forecastSearch = ForecastSearch(
                        input = location,
                        forecastResultSet = forecastResultSet,
                    )
                    lastForecastStore.save(forecastSearch)
                        .subscribeOn(schedulerProvider.io())
                        .subscribe()
                },
                { error -> ifViewAttached { it.showError(error, false) } }
            )
    }

    override fun destroy() {
        loadLastForecastDisposable?.dispose()
        getForecastDisposable?.dispose()
        super.destroy()
    }

    /**
     * Displays the given forecast data
     *
     * @param forecastResultSet object with forecast data to set and display
     */
    private fun setForecastResultSet(forecastResultSet: ForecastResultSet) {
        this.forecastResultSet = forecastResultSet
        ifViewAttached { view ->
            view.setData(forecastResultSet)
            view.showContent()
        }
    }
}
