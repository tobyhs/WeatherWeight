package io.github.tobyhs.weatherweight.forecast

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp

import io.github.tobyhs.weatherweight.R
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * Displays data for a [ForecastResultSet]
 *
 * @param forecastResultSet the [ForecastResultSet] to display data for
 */
@Composable
fun ForecastScreenContent(forecastResultSet: ForecastResultSet) {
    Column {
        Text(forecastResultSet.location, modifier = Modifier.testTag("locationFound"))
        Text(
            forecastResultSet.publicationTime.format(DateTimeFormatter.RFC_1123_DATE_TIME),
            modifier = Modifier.testTag("publicationTime"),
        )

        var tabIndex by remember { mutableIntStateOf(0) }
        val tabTitleResources = listOf(R.string.daily, R.string.hourly)
        ScrollableTabRow(tabIndex, modifier = Modifier.padding(top = 4.dp), edgePadding = 0.dp) {
            tabTitleResources.forEachIndexed { index, titleRes ->
                Tab(
                    text = { Text(stringResource(titleRes)) },
                    modifier = Modifier.testTag("forecastTab_${index}"),
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                )
            }
        }

        when (tabTitleResources[tabIndex]) {
            R.string.daily -> {
                for (forecast in forecastResultSet.dailyForecasts) {
                    DailyForecastCard(forecast)
                }
            }
            R.string.hourly -> {
                for (forecast in forecastResultSet.hourlyForecasts) {
                    HourlyForecastCard(forecast)
                }
            }
        }
    }
}

internal val previewDataForecastResultSet = ForecastResultSet(
    location = "Some City, ST",
    publicationTime = ZonedDateTime.of(2023, 1, 15, 12, 30, 0, 0, ZoneId.systemDefault()),
    dailyForecasts = previewDataDailyForecasts,
    hourlyForecasts = previewDataHourlyForecasts,
)

@PreviewScreenSizes
@Composable
private fun ForecastScreenContentPreview() {
    ForecastScreenContent(previewDataForecastResultSet)
}
