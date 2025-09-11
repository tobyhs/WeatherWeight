package io.github.tobyhs.weatherweight.forecast

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4

import io.github.tobyhs.weatherweight.test.ForecastResultSetFactory
import io.github.tobyhs.weatherweight.test.TestCompositionLocalProvider

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ForecastScreenContentTest {
    @get:Rule val composeRule = createComposeRule()

    @Test
    fun compose() {
        val restorationTester = StateRestorationTester(composeRule)
        restorationTester.setContent {
            TestCompositionLocalProvider {
                ForecastScreenContent(ForecastResultSetFactory.create())
            }
        }
        checkContent(composeRule)

        composeRule.onNodeWithTag("forecastTab_1").performClick()
        restorationTester.emulateSavedInstanceStateRestore()
        HourlyForecastCardTest.checkContent(composeRule)
    }

    companion object {
        fun checkContent(composeRule: ComposeTestRule) {
            composeRule.onNodeWithTag("locationFound").assertTextEquals("Oakland, CA, US")
            composeRule.onNodeWithTag("publicationTime")
                .assertTextEquals("Fri, 1 Feb 2019 12:00:00 GMT")

            composeRule.onNodeWithTag("forecastTab_1").performClick()
            HourlyForecastCardTest.checkContent(composeRule)
            listOf(
                "hour" to "04:00",
                "hourlyTemperature" to "67",
                "hourlyPrecipitationProbability" to "10%",
                "hourlyForecastDescription" to "Cloudy",
            ).forEach { (testTag, expectedText) ->
                composeRule.onAllNodesWithTag(testTag)[1].assertTextEquals(expectedText)
            }

            composeRule.onNodeWithTag("forecastTab_0").performClick()
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
        }
    }
}
