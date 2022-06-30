package io.github.tobyhs.weatherweight.forecast

import android.content.Intent
import android.net.Uri
import android.widget.TextView

import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules

import io.github.tobyhs.weatherweight.R
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet
import io.github.tobyhs.weatherweight.di.ViewModelFactoryProducer
import io.github.tobyhs.weatherweight.di.ViewModelFactoryProducerModule
import io.github.tobyhs.weatherweight.test.ForecastResultSetFactory
import io.github.tobyhs.weatherweight.ui.LoadState

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.Mockito
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

import org.robolectric.Shadows.shadowOf

@RunWith(AndroidJUnit4::class)
@UninstallModules(ViewModelFactoryProducerModule::class)
@HiltAndroidTest
class ForecastActivityTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val locationInputLiveData: MutableLiveData<String> = MutableLiveData("")
    private val forecastStatesLiveData: MutableLiveData<LoadState<ForecastResultSet>> = MutableLiveData()
    private val viewModel: ForecastViewModel = mock(ForecastViewModel::class.java).also {
        Mockito.`when`(it.locationInput).thenReturn(locationInputLiveData)
        Mockito.`when`(it.forecastState).thenReturn(forecastStatesLiveData)
    }

    @BindValue
    val viewModelFactoryProducer: ViewModelFactoryProducer = {
        viewModelFactory { initializer { viewModel } }
    }

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(ForecastActivity::class.java)

    @Test
    fun onCreate() {
        activityScenarioRule.scenario.onActivity { activity ->
            assertThat(activity.binding.locationSearch.isSubmitButtonEnabled, equalTo(true))
            assertThat(activity.binding.loadingView.isVisible, equalTo(false))
            assertThat(activity.binding.contentView.isVisible, equalTo(false))
            assertThat(activity.binding.errorView.isVisible, equalTo(false))
            verify(viewModel).loadLastForecast()
        }
    }

    @Test
    fun `onCreate with non-null savedInstanceState`() {
        val scenario = activityScenarioRule.scenario
        scenario.onActivity {
            // Clearing so we can check loadLastForecast isn't called on the 2nd onCreate
            clearInvocations(viewModel)
        }

        scenario.recreate()
        scenario.onActivity {
            verify(viewModel, never()).loadLastForecast()
        }
    }

    @Test
    fun `when viewModel locationInput changes`() {
        activityScenarioRule.scenario.onActivity { activity ->
            val location = "VM location changed"
            locationInputLiveData.value = location
            assertThat(activity.binding.locationSearch.query.toString(), equalTo(location))
        }
    }

    @Test
    fun `when viewModel forecastState is Loading`() {
        activityScenarioRule.scenario.onActivity { activity ->
            forecastStatesLiveData.value = LoadState.Loading()
            assertThat(activity.binding.loadingView.isVisible, equalTo(true))
            assertThat(activity.binding.contentView.isVisible, equalTo(false))
            assertThat(activity.binding.errorView.isVisible, equalTo(false))
        }
    }

    @Test
    fun `when viewModel forecastState is Content`() {
        activityScenarioRule.scenario.onActivity { activity ->
            val binding = activity.binding
            binding.forecastSwipeContainer.isRefreshing = true
            val forecastResultSet = ForecastResultSetFactory.create()
            forecastStatesLiveData.value = LoadState.Content(forecastResultSet)

            assertThat(binding.loadingView.isVisible, equalTo(false))
            assertThat(binding.contentView.isVisible, equalTo(true))
            assertThat(binding.errorView.isVisible, equalTo(false))

            assertThat(binding.locationFound.text.toString(), equalTo("Oakland, CA, US"))
            assertThat(binding.pubDate.text.toString(), equalTo("Fri, 1 Feb 2019 12:00:00 GMT"))
            assertThat(binding.forecastSwipeContainer.isRefreshing, equalTo(false))

            val recyclerView = binding.forecastRecyclerView
            recyclerView.measure(0, 0)
            recyclerView.layout(0, 0, 0, 10000)
            val layoutManager = recyclerView.layoutManager!!

            val forecasts = forecastResultSet.forecasts
            assertThat(layoutManager.itemCount, equalTo(forecasts.size))

            val view = layoutManager.findViewByPosition(0)!!
            val dayView = view.findViewById<TextView>(R.id.day)
            assertThat(dayView.text.toString(), equalTo("Fri"))
            val dateView = view.findViewById<TextView>(R.id.date)
            assertThat(dateView.text.toString(), equalTo("Feb 1"))
            val lowView = view.findViewById<TextView>(R.id.temperatureLow)
            assertThat(lowView.text.toString(), equalTo("60"))
            val highView = view.findViewById<TextView>(R.id.temperatureHigh)
            assertThat(highView.text.toString(), equalTo("65"))
            val descriptionView = view.findViewById<TextView>(R.id.description)
            assertThat(descriptionView.text.toString(), equalTo("Cloudy"))
        }
    }

    @Test
    fun `when viewModel forecastState is Error`() {
        activityScenarioRule.scenario.onActivity { activity ->
            val errorMessage = "test error"
            forecastStatesLiveData.value = LoadState.Error(Exception(errorMessage))
            assertThat(activity.binding.loadingView.isVisible, equalTo(false))
            assertThat(activity.binding.contentView.isVisible, equalTo(false))
            assertThat(activity.binding.errorView.isVisible, equalTo(true))
            assertThat(activity.binding.errorView.text.toString(), equalTo(errorMessage))
        }
    }

    @Test
    fun onQueryTextSubmit() {
        activityScenarioRule.scenario.onActivity { activity ->
            activity.binding.locationSearch.setQuery("Submit", true)
            verify(viewModel).search()
        }
    }

    @Test
    fun onQueryTextChange() {
        activityScenarioRule.scenario.onActivity { activity ->
            val location = "Query Changed"
            activity.binding.locationSearch.setQuery(location, false)
            assertThat(locationInputLiveData.value, equalTo(location))
        }
    }

    @Test
    fun onRefresh() {
        activityScenarioRule.scenario.onActivity { activity ->
            activity.onRefresh()
            verify(viewModel).search()
        }
    }

    @Test
    fun `clicking the AccuWeather link`() {
        activityScenarioRule.scenario.onActivity { activity ->
            activity.binding.accuweatherLogo.performClick()
            val actual = shadowOf(activity).nextStartedActivity
            val expected = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.accuweather.com"))
            assertThat(actual.filterEquals(expected), equalTo(true))
        }
    }
}
