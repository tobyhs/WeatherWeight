package io.github.tobyhs.weatherweight.test

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

import io.github.tobyhs.weatherweight.ui.LocalClock

import java.time.Clock
import java.time.ZoneId

@Composable
fun TestCompositionLocalProvider(content: @Composable () -> Unit) {
    val clock = Clock.system(ZoneId.of("America/Los_Angeles"))
    CompositionLocalProvider(LocalClock provides clock, content)
}
