package io.github.tobyhs.weatherweight.forecast

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4

import io.github.tobyhs.weatherweight.test.ForecastResultSetFactory

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ForecastScreenContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun compose() {
        composeRule.setContent {
            ForecastScreenContent(ForecastResultSetFactory.create())
        }

        checkContent(composeRule)
    }

    companion object {
        fun checkContent(composeRule: ComposeTestRule) {
            composeRule.onNodeWithTag("locationFound").assertTextEquals("Oakland, CA, US")
            composeRule.onNodeWithTag("publicationTime")
                .assertTextEquals("Fri, 1 Feb 2019 12:00:00 GMT")

            ForecastCardTest.checkContent(composeRule)
            composeRule.onAllNodesWithTag("dayOfWeek")[1].assertTextEquals("Sat")
            composeRule.onAllNodesWithTag("date")[1].assertTextEquals("Feb 2")
            composeRule.onAllNodesWithTag("lowTemperature")[1].assertTextEquals("58")
            composeRule.onAllNodesWithTag("highTemperature")[1].assertTextEquals("64")
            composeRule.onAllNodesWithTag("precipitationProbability")[1].assertTextEquals("70%")
            composeRule.onAllNodesWithTag("forecastDescription")[1].assertTextEquals("Showers")
        }
    }
}
