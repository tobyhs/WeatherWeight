package io.github.tobyhs.weatherweight.forecast

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.test.ext.junit.runners.AndroidJUnit4

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules

import io.github.tobyhs.weatherweight.R
import io.github.tobyhs.weatherweight.di.ViewModelFactoryProducer
import io.github.tobyhs.weatherweight.di.ViewModelFactoryProducerModule

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@UninstallModules(ViewModelFactoryProducerModule::class)
@HiltAndroidTest
class ForecastActivityTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val viewModel = mockk<ForecastViewModel>(relaxed = true) {
        every { locationInput } returns MutableLiveData("")
        every { forecastState } returns MutableLiveData()
    }

    @BindValue
    val viewModelFactoryProducer: ViewModelFactoryProducer = {
        viewModelFactory { initializer { viewModel } }
    }

    @get:Rule
    var composeRule = createAndroidComposeRule<ForecastActivity>()

    @Test
    fun onCreate() {
        verify { viewModel.loadLastForecast() }
        for (stringId in listOf(R.string.locationSearchHint, R.string.powered_by_accuweather)) {
            val contentDescription = composeRule.activity.getString(stringId)
            composeRule.onNodeWithContentDescription(contentDescription).assertExists()
        }
    }

    @Test
    fun `onCreate with non-null savedInstanceState`() {
        val scenario = composeRule.activityRule.scenario
        scenario.recreate()
        scenario.onActivity { verify(exactly = 1) { viewModel.loadLastForecast() } }
    }
}
