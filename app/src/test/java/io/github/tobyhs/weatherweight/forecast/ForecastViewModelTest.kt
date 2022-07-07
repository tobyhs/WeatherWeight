package io.github.tobyhs.weatherweight.forecast

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer

import com.github.tobyhs.rxsecretary.SchedulerProvider
import com.github.tobyhs.rxsecretary.TrampolineSchedulerProvider

import io.github.tobyhs.weatherweight.data.LocationNotFoundError
import io.github.tobyhs.weatherweight.data.WeatherRepository
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet
import io.github.tobyhs.weatherweight.data.model.ForecastSearch
import io.github.tobyhs.weatherweight.storage.LastForecastStore
import io.github.tobyhs.weatherweight.test.BaseTestCase
import io.github.tobyhs.weatherweight.test.ForecastResultSetFactory
import io.github.tobyhs.weatherweight.test.ForecastSearchFactory
import io.github.tobyhs.weatherweight.ui.LoadState

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Rule
import org.junit.Test

import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ForecastViewModelTest : BaseTestCase() {
    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val schedulerProvider: SchedulerProvider = TrampolineSchedulerProvider()
    private val weatherRepository = mock<WeatherRepository>()
    private val lastForecastStore = mock<LastForecastStore>()
    private val viewModel = ForecastViewModel(
        schedulerProvider,
        weatherRepository,
        lastForecastStore,
    )

    @Test
    fun `loadLastForecast when there is a previous forecast result`() {
        val forecastSearch = ForecastSearchFactory.create()
        whenever(lastForecastStore.get()).thenReturn(Maybe.just(forecastSearch))
        viewModel.loadLastForecast()
        assertThat(viewModel.locationInput.value, equalTo(forecastSearch.input))
        val forecastResultSet = forecastSearch.forecastResultSet
        assertThat(viewModel.forecastState.value, equalTo(LoadState.Content(forecastResultSet)))
    }

    @Test
    fun `loadLastForecast when there is an error`() {
        val error = Exception("LastForecastStore error")
        whenever(lastForecastStore.get()).thenReturn(Maybe.error(error))
        viewModel.loadLastForecast()
        assertThat(viewModel.forecastState.value, equalTo(LoadState.Error(error)))
    }

    @Test
    fun `loadLastForecast when there is no previous forecast result`() {
        whenever(lastForecastStore.get()).thenReturn(Maybe.empty())
        viewModel.loadLastForecast()
        assertThat(viewModel.locationInput.value, equalTo(""))
        assertThat(viewModel.forecastState.value, nullValue())
    }

    @Test
    fun `search on success`() {
        val previousGetForecastDisposable = mock<Disposable>()
        viewModel.getForecastDisposable = previousGetForecastDisposable

        val location = "San Francisco, CA"
        val forecastResultSet = ForecastResultSetFactory.create()
        whenever(weatherRepository.getForecast(location)).thenReturn(Single.just(forecastResultSet))
        viewModel.locationInput.value = location

        var completableSubscribed = false
        val saveCompletable = Completable.complete().doOnSubscribe { completableSubscribed = true }
        val forecastSearch = ForecastSearch(
            input = location,
            forecastResultSet = forecastResultSet,
        )
        whenever(lastForecastStore.save(forecastSearch)).thenReturn(saveCompletable)

        val forecastStates = searchAndObserveForecastStates()
        assertThat(forecastStates.size, equalTo(2))
        assertThat(forecastStates[0], instanceOf(LoadState.Loading::class.java))
        assertThat(forecastStates[1], equalTo(LoadState.Content(forecastResultSet)))

        verify(previousGetForecastDisposable).dispose()
        assertThat(viewModel.getForecastDisposable, not(previousGetForecastDisposable))
        assertThat(completableSubscribed, equalTo(true))
    }

    @Test
    fun `search on error`() {
        val location = "Parts Unknown"
        val error = LocationNotFoundError(location)
        whenever(weatherRepository.getForecast(location)).thenReturn(Single.error(error))
        viewModel.locationInput.value = location

        val forecastStates = searchAndObserveForecastStates()
        assertThat(forecastStates.size, equalTo(2))
        assertThat(forecastStates[0], instanceOf(LoadState.Loading::class.java))
        assertThat(forecastStates[1], equalTo(LoadState.Error(error)))
    }

    @Test
    fun onCleared() {
        val loadLastForecastDisposable = mock<Disposable>()
        val getForecastDisposable = mock<Disposable>()
        viewModel.loadLastForecastDisposable = loadLastForecastDisposable
        viewModel.getForecastDisposable = getForecastDisposable
        viewModel.onCleared()

        verify(loadLastForecastDisposable).dispose()
        verify(getForecastDisposable).dispose()
    }

    /**
     * Observes [ForecastViewModel.forecastState] and runs [ForecastViewModel.search]
     *
     * @return the [ForecastViewModel.forecastState] values that were emitted
     */
    private fun searchAndObserveForecastStates(): List<LoadState<ForecastResultSet>> {
        val forecastStates = mutableListOf<LoadState<ForecastResultSet>>()
        val forecastStateObserver = Observer<LoadState<ForecastResultSet>> { forecastState ->
            forecastStates.add(forecastState)
        }
        viewModel.forecastState.observeForever(forecastStateObserver)
        viewModel.search()
        viewModel.forecastState.removeObserver(forecastStateObserver)
        return forecastStates
    }
}
