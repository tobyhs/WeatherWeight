package io.github.tobyhs.weatherweight.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import java.time.Clock

@RunWith(AndroidJUnit4::class)
class LocalClockTest {
    @get:Rule val composeRule = createComposeRule()

    @Test
    fun current() {
        lateinit var clock: Clock
        composeRule.setContent {
            clock = LocalClock.current
        }
        assertThat(clock, equalTo(Clock.systemDefaultZone()))
    }
}
