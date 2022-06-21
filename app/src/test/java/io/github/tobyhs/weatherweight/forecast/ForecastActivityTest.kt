package io.github.tobyhs.weatherweight.forecast

import android.content.Intent
import android.net.Uri
import android.widget.TextView
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4

import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest

import io.github.tobyhs.weatherweight.R
import io.github.tobyhs.weatherweight.test.ForecastResultSetFactory

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.clearInvocations
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

import org.robolectric.Shadows.shadowOf

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ForecastActivityTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @BindValue
    val presenter: ForecastPresenter = mock(ForecastPresenter::class.java)

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(ForecastActivity::class.java)

    @Test
    fun onCreate() {
        activityScenarioRule.scenario.onActivity { activity ->
            assertThat(activity.binding.locationSearch.isSubmitButtonEnabled, equalTo(true))
        }
    }

    @Test
    fun onCreateWithNullSavedInstanceState() {
        activityScenarioRule.scenario.onActivity {
            verify(presenter).loadLastForecast()
        }
    }

    @Test
    fun onCreateWithPresentSavedInstanceState() {
        val scenario = activityScenarioRule.scenario
        scenario.onActivity {
            // Clearing so we can check loadLastForecast isn't called on the 2nd onCreate
            clearInvocations(presenter)
        }

        scenario.recreate()
        scenario.onActivity {
            verify(presenter, never()).loadLastForecast()
        }
    }

    @Test
    fun createPresenter() {
        activityScenarioRule.scenario.onActivity { activity ->
            assertThat(activity.createPresenter(), notNullValue())
        }
    }

    @Test
    fun getData() {
        activityScenarioRule.scenario.onActivity { activity ->
            val forecastResultSet = ForecastResultSetFactory.create()
            Mockito.`when`(presenter.forecastResultSet).thenReturn(forecastResultSet)

            assertThat(activity.data, equalTo(forecastResultSet))
        }
    }

    @Test
    fun setData() {
        activityScenarioRule.scenario.onActivity { activity ->
            val binding = activity.binding
            binding.forecastSwipeContainer.isRefreshing = true
            val forecastResultSet = ForecastResultSetFactory.create()
            activity.data = forecastResultSet

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
    fun setDataWithNull() {
        activityScenarioRule.scenario.onActivity { activity ->
            // To check that a NullPointerException isn't thrown
            activity.data = null
        }
    }

    @Test
    fun getErrorMessage() {
        activityScenarioRule.scenario.onActivity { activity ->
            val throwable = Throwable("test error")
            assertThat(activity.getErrorMessage(throwable, false), equalTo(throwable.toString()))
        }
    }

    @Test
    fun createViewState() {
        activityScenarioRule.scenario.onActivity { activity ->
            assertThat(activity.createViewState(), instanceOf(RetainingLceViewState::class.java))
        }
    }

    @Test
    fun setLocationInputText() {
        activityScenarioRule.scenario.onActivity { activity ->
            val location = "Saved City, SC"
            activity.setLocationInputText(location)

            assertThat(activity.binding.locationSearch.query.toString(), equalTo(location))
            assertThat(activity.binding.locationSearch.hasFocus(), equalTo(false))
            verify(presenter, never()).search(anyString())
        }
    }

    @Test
    fun onQueryTextSubmit() {
        activityScenarioRule.scenario.onActivity { activity ->
            val location = "San Francisco, CA"
            activity.binding.locationSearch.setQuery(location, true)
            verify(presenter).search(location)
        }
    }

    @Test
    fun onQueryTextChange() {
        activityScenarioRule.scenario.onActivity { activity ->
            assertThat(activity.onQueryTextChange("i"), equalTo(true))
        }
    }

    @Test
    fun onRefresh() {
        activityScenarioRule.scenario.onActivity { activity ->
            val location = "Current"
            activity.binding.locationSearch.setQuery(location, false)
            activity.onRefresh()
            verify(presenter).search(location)
        }
    }

    @Test
    fun openAttributionUrl() {
        activityScenarioRule.scenario.onActivity { activity ->
            activity.binding.accuweatherLogo.performClick()
            val actual = shadowOf(activity).nextStartedActivity
            val expected = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.accuweather.com"))
            assertThat(actual.filterEquals(expected), equalTo(true))
        }
    }
}
