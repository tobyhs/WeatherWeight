package io.github.tobyhs.weatherweight.ui

import androidx.compose.runtime.staticCompositionLocalOf

import java.time.Clock

/**
 * CompositionLocal for the default clock
 */
val LocalClock = staticCompositionLocalOf<Clock> { Clock.systemDefaultZone() }
