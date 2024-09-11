package io.github.tobyhs.weatherweight.forecast

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4

import io.github.tobyhs.weatherweight.test.ForecastResultSetFactory

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DailyForecastCardTest {
    @get:Rule val composeRule = createComposeRule()

    @Test
    fun compose() {
        composeRule.setContent {
            DailyForecastCard(ForecastResultSetFactory.createDailyForecasts()[0])
        }

        checkContent(composeRule)
    }

    companion object {
        fun checkContent(composeRule: ComposeTestRule) {
            composeRule.onAllNodesWithTag("dayOfWeek")[0].assertTextEquals("Fri")
            composeRule.onAllNodesWithTag("date")[0].assertTextEquals("Feb 1")
            composeRule.onAllNodesWithTag("lowTemperature")[0].assertTextEquals("60")
            composeRule.onAllNodesWithTag("highTemperature")[0].assertTextEquals("65")
            composeRule.onAllNodesWithTag("precipitationProbability")[0].assertTextEquals("10%")
            composeRule.onAllNodesWithTag("forecastDescription")[0].assertTextEquals("Cloudy")
        }
    }
}
