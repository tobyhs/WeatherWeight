package io.github.tobyhs.weatherweight.forecast

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4

import io.github.tobyhs.weatherweight.R
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet
import io.github.tobyhs.weatherweight.test.ForecastResultSetFactory
import io.github.tobyhs.weatherweight.test.TestCompositionLocalProvider
import io.github.tobyhs.weatherweight.ui.LoadState

import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify

import kotlinx.coroutines.flow.MutableStateFlow

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ForecastScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val locationInputFlow = MutableStateFlow("")
    private val forecastStateFlow: MutableStateFlow<LoadState<ForecastResultSet>?> = MutableStateFlow(null)
    private val viewModel = mockk<ForecastViewModel> {
        every { locationInput } returns locationInputFlow
        every { forecastState } returns forecastStateFlow
    }

    @Test
    fun `locationInput initially set`() {
        val location = "Initial Location"
        locationInputFlow.value = location
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
        assertThat(locationInputFlow.value, equalTo(location))

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
        forecastStateFlow.value = LoadState.Loading()
        setContent()
        checkLce("loading")
    }

    @Test
    fun content() {
        forecastStateFlow.value = LoadState.Content(ForecastResultSetFactory.create())
        setContent()
        checkLce("locationFound")
        ForecastScreenContentTest.checkContent(composeRule)
    }

    @Test
    fun error() {
        val errorMessage = "Test Error"
        forecastStateFlow.value = LoadState.Error(Error(errorMessage))
        setContent()
        checkLce("error")
        composeRule.onNodeWithTag("error").assertTextEquals("Error: $errorMessage")
    }

    private fun setContent() {
        composeRule.setContent {
            TestCompositionLocalProvider {
                ForecastScreen(viewModel)
            }
        }
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
