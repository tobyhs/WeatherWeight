package io.github.tobyhs.weatherweight.forecast

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer

import io.github.tobyhs.weatherweight.data.LocationNotFoundError
import io.github.tobyhs.weatherweight.data.WeatherCoroutinesRepository
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet
import io.github.tobyhs.weatherweight.data.model.ForecastSearch
import io.github.tobyhs.weatherweight.storage.LastForecastCoroutinesStore
import io.github.tobyhs.weatherweight.test.ForecastResultSetFactory
import io.github.tobyhs.weatherweight.test.ForecastSearchFactory
import io.github.tobyhs.weatherweight.ui.LoadState

import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ForecastViewModelTest {
    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val weatherRepository = mockk<WeatherCoroutinesRepository>()
    private val lastForecastStore = mockk<LastForecastCoroutinesStore>()
    private val viewModel = ForecastViewModel(weatherRepository, lastForecastStore)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadLastForecast when there is a previous forecast result`() = runTest(testDispatcher) {
        val forecastSearch = ForecastSearchFactory.create()
        coEvery { lastForecastStore.get() } returns forecastSearch
        viewModel.loadLastForecast()
        advanceUntilIdle()
        assertThat(viewModel.locationInput.value, equalTo(forecastSearch.input))
        val forecastResultSet = forecastSearch.forecastResultSet
        assertThat(viewModel.forecastState.value, equalTo(LoadState.Content(forecastResultSet)))
    }

    @Test
    fun `loadLastForecast when there is an error`() = runTest(testDispatcher) {
        val error = Exception("LastForecastStore error")
        coEvery { lastForecastStore.get() } throws error
        viewModel.loadLastForecast()
        advanceUntilIdle()
        assertThat(viewModel.forecastState.value, equalTo(LoadState.Error(error)))
    }

    @Test
    fun `loadLastForecast when there is no previous forecast result`() = runTest(testDispatcher) {
        coEvery { lastForecastStore.get() } returns null
        viewModel.loadLastForecast()
        advanceUntilIdle()
        assertThat(viewModel.locationInput.value, equalTo(""))
        assertThat(viewModel.forecastState.value, nullValue())
    }

    @Test
    fun `search on success`() = runTest(testDispatcher) {
        val location = "San Francisco, CA"
        val forecastResultSet = ForecastResultSetFactory.create()
        coEvery { weatherRepository.getForecast(location) } returns forecastResultSet
        val forecastSearch = ForecastSearch(
            input = location,
            forecastResultSet = forecastResultSet,
        )
        coJustRun { lastForecastStore.save(forecastSearch) }

        viewModel.locationInput.value = location
        val forecastStates = searchAndObserveForecastStates()

        assertThat(forecastStates.size, equalTo(2))
        assertThat(forecastStates[0], instanceOf(LoadState.Loading::class.java))
        assertThat(forecastStates[1], equalTo(LoadState.Content(forecastResultSet)))
        coVerify(exactly = 1) { lastForecastStore.save(forecastSearch) }
    }

    @Test
    fun `search on error`() = runTest(testDispatcher) {
        val location = "Parts Unknown"
        val error = LocationNotFoundError(location)
        coEvery { weatherRepository.getForecast(location) } throws error
        viewModel.locationInput.value = location

        val forecastStates = searchAndObserveForecastStates()
        assertThat(forecastStates.size, equalTo(2))
        assertThat(forecastStates[0], instanceOf(LoadState.Loading::class.java))
        assertThat(forecastStates[1], equalTo(LoadState.Error(error)))
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
        runTest(testDispatcher) {
            viewModel.search()
            advanceUntilIdle()
        }
        viewModel.forecastState.removeObserver(forecastStateObserver)
        return forecastStates
    }
}
