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

import io.mockk.every
import io.mockk.mockk

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Rule
import org.junit.Test

class ForecastViewModelTest : BaseTestCase() {
    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val schedulerProvider: SchedulerProvider = TrampolineSchedulerProvider()
    private val weatherRepository = mockk<WeatherRepository>()
    private val lastForecastStore = mockk<LastForecastStore>()
    private val viewModel = ForecastViewModel(
        schedulerProvider,
        weatherRepository,
        lastForecastStore,
    )

    @Test
    fun `loadLastForecast when there is a previous forecast result`() {
        val forecastSearch = ForecastSearchFactory.create()
        every { lastForecastStore.get() } returns Maybe.just(forecastSearch)
        viewModel.loadLastForecast()
        assertThat(viewModel.locationInput.value, equalTo(forecastSearch.input))
        val forecastResultSet = forecastSearch.forecastResultSet
        assertThat(viewModel.forecastState.value, equalTo(LoadState.Content(forecastResultSet)))
    }

    @Test
    fun `loadLastForecast when there is an error`() {
        val error = Exception("LastForecastStore error")
        every { lastForecastStore.get() } returns Maybe.error(error)
        viewModel.loadLastForecast()
        assertThat(viewModel.forecastState.value, equalTo(LoadState.Error(error)))
    }

    @Test
    fun `loadLastForecast when there is no previous forecast result`() {
        every { lastForecastStore.get() } returns Maybe.empty()
        viewModel.loadLastForecast()
        assertThat(viewModel.locationInput.value, equalTo(""))
        assertThat(viewModel.forecastState.value, nullValue())
    }

    @Test
    fun `search on success`() {
        val location = "San Francisco, CA"
        val forecastResultSet = ForecastResultSetFactory.create()
        every { weatherRepository.getForecast(location) } returns Single.just(forecastResultSet)
        viewModel.locationInput.value = location

        var completableSubscribed = false
        val saveCompletable = Completable.complete().doOnSubscribe { completableSubscribed = true }
        val forecastSearch = ForecastSearch(
            input = location,
            forecastResultSet = forecastResultSet,
        )
        every { lastForecastStore.save(forecastSearch) } returns saveCompletable

        val forecastStates = searchAndObserveForecastStates()
        assertThat(forecastStates.size, equalTo(2))
        assertThat(forecastStates[0], instanceOf(LoadState.Loading::class.java))
        assertThat(forecastStates[1], equalTo(LoadState.Content(forecastResultSet)))

        assertThat(completableSubscribed, equalTo(true))
    }

    @Test
    fun `search when getForecastDisposable is present`() {
        val firstGetForecastDisposable = Disposable.empty()
        stubGetForecastChain(firstGetForecastDisposable, Disposable.empty())

        viewModel.search()
        assertThat(firstGetForecastDisposable.isDisposed, equalTo(false))
        viewModel.search()
        assertThat(firstGetForecastDisposable.isDisposed, equalTo(true))
    }

    @Test
    fun `search on error`() {
        val location = "Parts Unknown"
        val error = LocationNotFoundError(location)
        every { weatherRepository.getForecast(location) } returns Single.error(error)
        viewModel.locationInput.value = location

        val forecastStates = searchAndObserveForecastStates()
        assertThat(forecastStates.size, equalTo(2))
        assertThat(forecastStates[0], instanceOf(LoadState.Loading::class.java))
        assertThat(forecastStates[1], equalTo(LoadState.Error(error)))
    }

    @Test
    fun onCleared() {
        val loadLastForecastDisposable = Disposable.empty()
        every {
            lastForecastStore.get()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(any(), any())
        } returns loadLastForecastDisposable
        viewModel.loadLastForecast()

        val getForecastDisposable = Disposable.empty()
        stubGetForecastChain(getForecastDisposable)
        viewModel.search()

        assertThat(loadLastForecastDisposable.isDisposed, equalTo(false))
        assertThat(getForecastDisposable.isDisposed, equalTo(false))
        viewModel.onCleared()
        assertThat(loadLastForecastDisposable.isDisposed, equalTo(true))
        assertThat(getForecastDisposable.isDisposed, equalTo(true))
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

    private fun stubGetForecastChain(
        disposableToReturn: Disposable, vararg otherDisposablesToReturn: Disposable
    ) {
        val location = "Stub City, CA"
        viewModel.locationInput.value = location
        every {
            weatherRepository.getForecast(location)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(any(), any())
        } returnsMany listOf(disposableToReturn, *otherDisposablesToReturn)
    }
}
