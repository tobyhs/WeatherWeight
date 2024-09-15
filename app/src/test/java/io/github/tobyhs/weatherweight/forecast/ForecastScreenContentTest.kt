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
    @get:Rule val composeRule = createComposeRule()

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

            DailyForecastCardTest.checkContent(composeRule)
            listOf(
                "dayOfWeek" to "Sat",
                "date" to "Feb 2",
                "lowTemperature" to "58",
                "highTemperature" to "64",
                "precipitationProbability" to "70%",
                "forecastDescription" to "Showers",
            ).forEach { (testTag, expectedText) ->
                composeRule.onAllNodesWithTag(testTag)[1].assertTextEquals(expectedText)
            }

            HourlyForecastCardTest.checkContent(composeRule)
            listOf(
                "hour" to "12:00",
                "hourlyTemperature" to "67",
                "hourlyPrecipitationProbability" to "10%",
                "hourlyForecastDescription" to "Cloudy",
            ).forEach { (testTag, expectedText) ->
                composeRule.onAllNodesWithTag(testTag)[1].assertTextEquals(expectedText)
            }
        }
    }
}
