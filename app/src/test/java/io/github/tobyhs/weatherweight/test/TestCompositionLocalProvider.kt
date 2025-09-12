package io.github.tobyhs.weatherweight.test

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.junit4.ComposeContentTestRule

import io.github.tobyhs.weatherweight.ui.LocalClock

import java.time.Clock
import java.time.ZoneId

@Composable
fun TestCompositionLocalProvider(content: @Composable () -> Unit) {
    val clock = Clock.system(ZoneId.of("America/Los_Angeles"))
    CompositionLocalProvider(LocalClock provides clock, content)
}

fun setContentWithTestProvider(composeRule: ComposeContentTestRule, content: @Composable () -> Unit) {
    composeRule.setContent { TestCompositionLocalProvider(content) }
}
