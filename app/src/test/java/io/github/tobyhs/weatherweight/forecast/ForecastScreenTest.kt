package io.github.tobyhs.weatherweight.forecast

import android.content.Intent

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.ext.junit.runners.AndroidJUnit4

import io.github.tobyhs.weatherweight.R
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet
import io.github.tobyhs.weatherweight.test.ForecastResultSetFactory
import io.github.tobyhs.weatherweight.ui.LoadState

import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify

import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ForecastScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val locationInputLiveData: MutableLiveData<String> = MutableLiveData("")
    private val forecastStateLiveData: MutableLiveData<LoadState<ForecastResultSet>> = MutableLiveData()
    private val viewModel = mockk<ForecastViewModel> {
        every { locationInput } returns locationInputLiveData
        every { forecastState } returns forecastStateLiveData
    }

    @Test
    fun `locationInput initially set`() {
        val location = "Initial Location"
        locationInputLiveData.value = location
        lateinit var inputContentDescription: String
        composeRule.setContent {
            ForecastScreen(viewModel)
            inputContentDescription = stringResource(R.string.locationSearchHint)
        }
        composeRule.onNodeWithContentDescription(inputContentDescription)
            .assertTextContains(location)
    }

    @Test
    fun `entering a search on the keyboard`() {
        lateinit var inputContentDescription: String
        composeRule.setContent {
            ForecastScreen(viewModel)
            inputContentDescription = stringResource(R.string.locationSearchHint)
        }
        val locationInputNode = composeRule.onNodeWithContentDescription(inputContentDescription)
        val location = "Search City"
        locationInputNode.performTextInput(location)
        assertThat(locationInputLiveData.value, equalTo(location))

        justRun { viewModel.search() }
        locationInputNode.performImeAction()
        verify { viewModel.search() }
    }

    @Test
    fun `clicking the search icon`() {
        lateinit var buttonContentDescription: String
        composeRule.setContent {
            ForecastScreen(viewModel)
            buttonContentDescription = stringResource(R.string.submitLocation)
        }
        justRun { viewModel.search() }
        composeRule.onNodeWithContentDescription(buttonContentDescription).performClick()
        verify { viewModel.search() }
    }

    @Test
    fun initialState() {
        setContent()
        checkLce(null)
    }

    @Test
    fun loading() {
        forecastStateLiveData.value = LoadState.Loading()
        setContent()
        checkLce("loading")
    }

    @Test
    fun content() {
        forecastStateLiveData.value = LoadState.Content(ForecastResultSetFactory.create())
        setContent()
        checkLce("locationFound")
        ForecastScreenContentTest.checkContent(composeRule)
    }

    @Test
    fun error() {
        val errorMessage = "Test Error"
        forecastStateLiveData.value = LoadState.Error(Error(errorMessage))
        setContent()
        checkLce("error")
        composeRule.onNodeWithTag("error").assertTextEquals(errorMessage)
    }

    @Test
    fun `clicking the AccuWeather image`() {
        Intents.init()
        lateinit var accuweatherContentDescription: String
        composeRule.setContent {
            ForecastScreen(viewModel)
            accuweatherContentDescription = stringResource(R.string.powered_by_accuweather)
        }
        composeRule.onNodeWithContentDescription(accuweatherContentDescription).performClick()
        Intents.intended(
            allOf(hasAction(Intent.ACTION_VIEW), hasData("https://www.accuweather.com"))
        )
    }

    private fun setContent() {
        composeRule.setContent { ForecastScreen(viewModel) }
    }

    private fun checkLce(expectedTestTag: String?) {
        for (testTag in listOf("loading", "locationFound", "error")) {
            val node = composeRule.onNodeWithTag(testTag)
            if (testTag == expectedTestTag) {
                node.assertExists()
            } else {
                node.assertDoesNotExist()
            }
        }
    }
}
