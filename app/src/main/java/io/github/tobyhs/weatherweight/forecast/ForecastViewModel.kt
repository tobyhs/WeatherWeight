package io.github.tobyhs.weatherweight.forecast

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.github.tobyhs.rxsecretary.SchedulerProvider

import dagger.hilt.android.lifecycle.HiltViewModel

import io.github.tobyhs.weatherweight.data.WeatherRepository
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet
import io.github.tobyhs.weatherweight.data.model.ForecastSearch
import io.github.tobyhs.weatherweight.storage.LastForecastStore
import io.github.tobyhs.weatherweight.ui.LoadState

import io.reactivex.rxjava3.disposables.Disposable

import javax.inject.Inject

/**
 * View model for [ForecastActivity]
 */
@HiltViewModel
class ForecastViewModel
/**
 * @param schedulerProvider RX scheduler provider
 * @param weatherRepository weather repository object to obtain forecast data
 * @param lastForecastStore store to save or get the last forecast
 */ @Inject constructor(
    private val schedulerProvider: SchedulerProvider,
    private val weatherRepository: WeatherRepository,
    private val lastForecastStore: LastForecastStore,
) : ViewModel() {
    /** location input text */
    val locationInput: MutableLiveData<String> = MutableLiveData("")

    private val _forecastState: MutableLiveData<LoadState<ForecastResultSet>> = MutableLiveData()
    /** Loading state of the forecast result */
    val forecastState: LiveData<LoadState<ForecastResultSet>> = _forecastState

    @VisibleForTesting var loadLastForecastDisposable: Disposable? = null
    @VisibleForTesting var getForecastDisposable: Disposable? = null

    /**
     * Loads the last [ForecastSearch] that was saved when the activity first starts.
     */
    fun loadLastForecast() {
        loadLastForecastDisposable = lastForecastStore.get()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe(
                { forecastSearch ->
                    locationInput.value = forecastSearch.input
                    _forecastState.value = LoadState.Content(forecastSearch.forecastResultSet)
                },
                { error -> _forecastState.value = LoadState.Error(error) }
            )
    }

    /**
     * Obtains forecast data for the location in [locationInput]
     */
    fun search() {
        _forecastState.value = LoadState.Loading()
        getForecastDisposable?.dispose()
        val location = locationInput.value.toString()

        getForecastDisposable = weatherRepository.getForecast(location)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe(
                { forecastResultSet ->
                    _forecastState.value = LoadState.Content(forecastResultSet)
                    val forecastSearch = ForecastSearch(
                        input = location,
                        forecastResultSet = forecastResultSet,
                    )
                    lastForecastStore.save(forecastSearch)
                        .subscribeOn(schedulerProvider.io())
                        .subscribe()
                },
                { error -> _forecastState.value = LoadState.Error(error) }
            )
    }

    public override fun onCleared() {
        loadLastForecastDisposable?.dispose()
        getForecastDisposable?.dispose()
        super.onCleared()
    }
}
