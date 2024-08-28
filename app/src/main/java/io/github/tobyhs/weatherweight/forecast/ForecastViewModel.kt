package io.github.tobyhs.weatherweight.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel

import io.github.tobyhs.weatherweight.data.WeatherCoroutinesRepository
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet
import io.github.tobyhs.weatherweight.data.model.ForecastSearch
import io.github.tobyhs.weatherweight.storage.LastForecastCoroutinesStore
import io.github.tobyhs.weatherweight.ui.LoadState

import kotlinx.coroutines.launch

import javax.inject.Inject

/**
 * View model for [ForecastActivity]
 */
@HiltViewModel
class ForecastViewModel
/**
 * @param weatherRepository weather repository object to obtain forecast data
 * @param lastForecastStore store to save or get the last forecast
 */ @Inject constructor(
    private val weatherRepository: WeatherCoroutinesRepository,
    private val lastForecastStore: LastForecastCoroutinesStore,
) : ViewModel() {
    /** location input text */
    val locationInput: MutableLiveData<String> = MutableLiveData("")

    private val _forecastState: MutableLiveData<LoadState<ForecastResultSet>> = MutableLiveData()
    /** Loading state of the forecast result */
    val forecastState: LiveData<LoadState<ForecastResultSet>> = _forecastState

    /**
     * Loads the last [ForecastSearch] that was saved when the activity first starts.
     */
    fun loadLastForecast() {
        viewModelScope.launch {
            try {
                val forecastSearch = lastForecastStore.get()
                forecastSearch?.let {
                    locationInput.value = forecastSearch.input
                    _forecastState.value = LoadState.Content(forecastSearch.forecastResultSet)
                }
            } catch (exception: Exception) {
                _forecastState.value = LoadState.Error(exception)
            }
        }
    }

    /**
     * Obtains forecast data for the location in [locationInput]
     */
    fun search() {
        _forecastState.value = LoadState.Loading()
        val location = locationInput.value.toString()

        viewModelScope.launch {
            try {
                val forecastResultSet = weatherRepository.getForecast(location)
                _forecastState.value = LoadState.Content(forecastResultSet)
                val forecastSearch = ForecastSearch(
                    input = location,
                    forecastResultSet = forecastResultSet,
                )
                lastForecastStore.save(forecastSearch)
            } catch (exception: Exception) {
                _forecastState.value = LoadState.Error(exception)
            }
        }
    }
}
