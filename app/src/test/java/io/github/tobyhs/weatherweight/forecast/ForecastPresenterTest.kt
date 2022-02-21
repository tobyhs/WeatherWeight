package io.github.tobyhs.weatherweight.forecast

import java.util.concurrent.atomic.AtomicBoolean

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

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Before
import org.junit.Test

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class ForecastPresenterTest : BaseTestCase() {
    private val schedulerProvider: SchedulerProvider = TrampolineSchedulerProvider()
    @Mock private lateinit var weatherRepository: WeatherRepository
    @Mock private lateinit var lastForecastStore: LastForecastStore
    @Mock private lateinit var view: ForecastContract.View
    private lateinit var presenter: ForecastPresenter

    @Before
    fun setup() {
        presenter = ForecastPresenter(schedulerProvider, weatherRepository, lastForecastStore)
        presenter.attachView(view)
    }

    @Test
    fun loadLastForecastOnSuccess() {
        val forecastSearch = ForecastSearchFactory.create()
        Mockito.`when`(lastForecastStore.get()).thenReturn(Maybe.just(forecastSearch))
        presenter.loadLastForecast()
        checkForecastResultSet(forecastSearch.forecastResultSet)
        verify(view).setLocationInputText(forecastSearch.input)
    }

    @Test
    fun loadLastForecastOnError() {
        val error = Exception("testing")
        Mockito.`when`(lastForecastStore.get()).thenReturn(Maybe.error(error))
        presenter.loadLastForecast()
        verify(view).showError(error, false)
    }

    @Test
    fun loadLastForecastOnComplete() {
        Mockito.`when`(lastForecastStore.get()).thenReturn(Maybe.empty())
        presenter.loadLastForecast()
        verify(view).showContent()
    }

    @Test
    fun searchOnSuccess() {
        val location = "Oakland, CA"
        val forecastResultSet = ForecastResultSetFactory.create()
        Mockito.`when`(weatherRepository.getForecast(location))
            .thenReturn(Single.just(forecastResultSet))

        val completableSubscribed = AtomicBoolean(false)
        val saveCompletable = Completable.complete()
            .doOnSubscribe { completableSubscribed.set(true) }
        val forecastSearch = ForecastSearch(
            input = location,
            forecastResultSet = forecastResultSet,
        )
        Mockito.`when`(lastForecastStore.save(forecastSearch)).thenReturn(saveCompletable)

        presenter.search(location)
        verify(view).showLoading(false)
        checkForecastResultSet(forecastResultSet)
        assertThat(completableSubscribed.get(), equalTo(true))
    }

    @Test
    fun searchWithExistingGetForecastDisposable() {
        val getForecastDisposable = mock(Disposable::class.java)
        presenter.getForecastDisposable = getForecastDisposable
        val location = "Berkeley, CA"
        stubSearch(location)
        presenter.search(location)

        verify(getForecastDisposable).dispose()
        assertThat(presenter.getForecastDisposable, not(getForecastDisposable))
    }

    @Test
    fun searchOnError() {
        val location = "Parts Unknown"
        val locationError = LocationNotFoundError(location)
        val single = Single.error<ForecastResultSet>(locationError)
        Mockito.`when`(weatherRepository.getForecast(location)).thenReturn(single)
        presenter.search(location)

        verify(view).showLoading(false)
        assertThat(presenter.forecastResultSet, nullValue())
        verify(view).showError(locationError, false)
    }

    @Test
    fun destroyWhenDisposablesAreNull() {
        // Just to check there are no NPEs
        presenter.destroy()
    }

    @Test
    fun destroyWhenDisposablesAreNonNull() {
        val loadLastForecastDisposable = mock(Disposable::class.java)
        val getForecastDisposable = mock(Disposable::class.java)
        presenter.loadLastForecastDisposable = loadLastForecastDisposable
        presenter.getForecastDisposable = getForecastDisposable
        presenter.destroy()

        verify(loadLastForecastDisposable).dispose()
        verify(getForecastDisposable).dispose()
    }

    /**
     * Checks that the given forecast result set was set on the presenter
     *
     * @param forecastResultSet [ForecastResultSet] to check against
     */
    private fun checkForecastResultSet(forecastResultSet: ForecastResultSet) {
        assertThat(presenter.forecastResultSet, equalTo(forecastResultSet))
        verify(view).setData(forecastResultSet)
        verify(view).showContent()
    }

    /**
     * Stubs the dependencies in [ForecastPresenter.search]
     *
     * @param location location to stub a forecast for
     */
    private fun stubSearch(location: String) {
        val forecastResultSet = ForecastResultSetFactory.create()
        Mockito.`when`(weatherRepository.getForecast(location))
            .thenReturn(Single.just(forecastResultSet))

        val saveCompletable = Completable.complete()
        val forecastSearch = ForecastSearch(
            input = location,
            forecastResultSet = forecastResultSet,
        )
        Mockito.`when`(lastForecastStore.save(forecastSearch)).thenReturn(saveCompletable)
    }
}
