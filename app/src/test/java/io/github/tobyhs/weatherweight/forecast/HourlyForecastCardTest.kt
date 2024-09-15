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
class HourlyForecastCardTest {
    @get:Rule val composeRule = createComposeRule()

    @Test
    fun compose() {
        composeRule.setContent {
            HourlyForecastCard(ForecastResultSetFactory.createHourlyForecasts()[0])
        }
        checkContent(composeRule)
    }

    companion object {
        fun checkContent(composeRule: ComposeTestRule) {
            composeRule.onAllNodesWithTag("hour")[0].assertTextEquals("11:00")
            composeRule.onAllNodesWithTag("hourlyTemperature")[0].assertTextEquals("63")
            composeRule.onAllNodesWithTag("hourlyPrecipitationProbability")[0].assertTextEquals("5%")
            composeRule.onAllNodesWithTag("hourlyForecastDescription")[0].assertTextEquals("Mostly clear")
        }
    }
}
