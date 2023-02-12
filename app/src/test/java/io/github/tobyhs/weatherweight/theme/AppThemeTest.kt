package io.github.tobyhs.weatherweight.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppThemeTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `light theme`() {
        testTheme(useDarkTheme = false, lightColorScheme())
    }

    @Test
    fun `dark theme`() {
        testTheme(useDarkTheme = true, darkColorScheme())
    }

    private fun testTheme(useDarkTheme: Boolean, expectedColorScheme: ColorScheme) {
        var colorScheme: ColorScheme? = null
        var contentColor: Color? = null
        composeRule.setContent {
            AppTheme(useDarkTheme = useDarkTheme) {
                colorScheme = MaterialTheme.colorScheme
                contentColor = LocalContentColor.current
            }
        }

        for (property in COLOR_SCHEME_PROPERTIES) {
            assertThat(property.get(colorScheme!!), equalTo(property.get(expectedColorScheme)))
        }
        assertThat(contentColor, equalTo(expectedColorScheme.onSurface))
    }
}

private val COLOR_SCHEME_PROPERTIES = listOf(
    ColorScheme::primary,
    ColorScheme::onPrimary,
    ColorScheme::primaryContainer,
    ColorScheme::onPrimaryContainer,
    ColorScheme::inversePrimary,
    ColorScheme::secondary,
    ColorScheme::onSecondary,
    ColorScheme::secondaryContainer,
    ColorScheme::onSecondaryContainer,
    ColorScheme::tertiary,
    ColorScheme::onTertiary,
    ColorScheme::tertiaryContainer,
    ColorScheme::onTertiaryContainer,
    ColorScheme::background,
    ColorScheme::onBackground,
    ColorScheme::surface,
    ColorScheme::onSurface,
    ColorScheme::surfaceVariant,
    ColorScheme::onSurfaceVariant,
    ColorScheme::surfaceTint,
    ColorScheme::inverseSurface,
    ColorScheme::inverseOnSurface,
    ColorScheme::error,
    ColorScheme::onError,
    ColorScheme::errorContainer,
    ColorScheme::onErrorContainer,
    ColorScheme::outline,
    ColorScheme::outlineVariant,
    ColorScheme::scrim,
)
