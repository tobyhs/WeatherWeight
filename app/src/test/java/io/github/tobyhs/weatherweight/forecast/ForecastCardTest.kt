package io.github.tobyhs.weatherweight.forecast

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4

import io.github.tobyhs.weatherweight.test.ForecastResultSetFactory

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ForecastCardTest {
    @get:Rule val composeRule = createComposeRule()

    @Test
    fun compose() {
        composeRule.setContent {
            ForecastCard(ForecastResultSetFactory.createForecasts()[0])
        }

        composeRule.onNodeWithTag("dayOfWeek").assertTextEquals("Fri")
        composeRule.onNodeWithTag("date").assertTextEquals("Feb 1")
        composeRule.onNodeWithTag("lowTemperature").assertTextEquals("60")
        composeRule.onNodeWithTag("highTemperature").assertTextEquals("65")
        composeRule.onNodeWithTag("precipitationProbability").assertTextEquals("10%")
        composeRule.onNodeWithTag("forecastDescription").assertTextEquals("Cloudy")
    }
}
