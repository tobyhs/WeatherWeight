package io.github.tobyhs.weatherweight.forecast

import android.content.Intent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.hasChildCount
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParentIndex
import androidx.test.espresso.matcher.ViewMatchers.withResourceName
import androidx.test.espresso.matcher.ViewMatchers.withText
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

import org.hamcrest.CoreMatchers.allOf
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

@RunWith(AndroidJUnit4::class)
@UninstallModules(ViewModelFactoryProducerModule::class)
@HiltAndroidTest
class ForecastActivityTest {
    @get:Rule val hiltRule = HiltAndroidRule(this)

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

    @get:Rule val activityScenarioRule = ActivityScenarioRule(ForecastActivity::class.java)

    @Test
    fun onCreate() {
        checkLceVisibilities()
        verify(viewModel).loadLastForecast()
    }

    @Test
    fun `onCreate with non-null savedInstanceState`() {
        val scenario = activityScenarioRule.scenario
        scenario.onActivity {
            // Clearing so we can check loadLastForecast isn't called on the 2nd onCreate
            clearInvocations(viewModel)
        }

        scenario.recreate()
        scenario.onActivity { verify(viewModel, never()).loadLastForecast() }
    }

    @Test
    fun `when viewModel locationInput changes`() {
        val location = "VM location changed"
        locationInputLiveData.value = location
        onView(locationSearchInputMatcher).check(matches(withText(location)))
    }

    @Test
    fun `when viewModel forecastState is Loading`() {
        forecastStatesLiveData.value = LoadState.Loading()
        checkLceVisibilities(R.id.loadingView)
    }

    @Test
    fun `when viewModel forecastState is Content`() {
        val forecastResultSet = ForecastResultSetFactory.create()
        forecastStatesLiveData.value = LoadState.Content(forecastResultSet)

        checkLceVisibilities(R.id.contentView)
        onView(withId(R.id.locationFound)).check(matches(withText("Oakland, CA, US")))
        onView(withId(R.id.pubDate)).check(matches(withText("Fri, 1 Feb 2019 12:00:00 GMT")))

        val forecastLength = forecastResultSet.forecasts.size
        onView(withId(R.id.forecastRecyclerView)).check(matches(hasChildCount(forecastLength)))

        val firstForecastCardMatcher = allOf(withId(R.id.forecastCard), withParentIndex(0))
        val descendantMatcher = isDescendantOfA(firstForecastCardMatcher)
        onView(allOf(descendantMatcher, withId(R.id.day))).check(matches(withText("Fri")))
        onView(allOf(descendantMatcher, withId(R.id.date))).check(matches(withText("Feb 1")))
        onView(allOf(descendantMatcher, withId(R.id.temperatureLow))).check(matches(withText("60")))
        onView(allOf(descendantMatcher, withId(R.id.temperatureHigh))).check(matches(withText("65")))
        onView(allOf(descendantMatcher, withId(R.id.description))).check(matches(withText("Cloudy")))
    }

    @Test
    fun `when viewModel forecastState is Error`() {
        val errorMessage = "test error"
        forecastStatesLiveData.value = LoadState.Error(Exception(errorMessage))
        checkLceVisibilities(R.id.errorView)
        onView(withId(R.id.errorView)).check(matches(withText(errorMessage)))
    }

    @Test
    fun onQueryTextSubmit() {
        onView(locationSearchInputMatcher).perform(typeText("Submit"))
        onView(allOf(
            isDescendantOfA(withId(R.id.locationSearch)),
            withResourceName("search_go_btn")
        )).perform(click())
        verify(viewModel).search()
    }

    @Test
    fun onQueryTextChange() {
        val location = "Query Changed"
        onView(locationSearchInputMatcher).perform(typeText(location))
        assertThat(locationInputLiveData.value, equalTo(location))
    }

    @Test
    fun onRefresh() {
        // swipeDown doesn't work on SwipeRefreshLayout in Robolectric
        // see https://github.com/robolectric/robolectric/issues/5375
        activityScenarioRule.scenario.onActivity { activity ->
            activity.onRefresh()
            verify(viewModel).search()
        }
    }

    @Test
    fun `clicking the AccuWeather logo`() {
        Intents.init()
        onView(withId(R.id.accuweatherLogo)).perform(click())
        intended(allOf(hasAction(Intent.ACTION_VIEW), hasData("https://www.accuweather.com")))
    }

    /**
     * Checks that the loading, content, and error views for the forecast result have the correct
     * visibility.
     *
     * @param visibleResourceId resource ID of view that is expected to be visible
     */
    private fun checkLceVisibilities(visibleResourceId: Int? = null) {
        for (resourceId in listOf(R.id.loadingView, R.id.contentView, R.id.errorView)) {
            val expectedVisibility = if (resourceId == visibleResourceId) {
                Visibility.VISIBLE
            } else {
                Visibility.GONE
            }
            onView(withId(resourceId)).check(matches(withEffectiveVisibility(expectedVisibility)))
        }
    }

    companion object {
        private val locationSearchInputMatcher = allOf(
            isDescendantOfA(withId(R.id.locationSearch)),
            withResourceName("search_src_text")
        )
    }
}
