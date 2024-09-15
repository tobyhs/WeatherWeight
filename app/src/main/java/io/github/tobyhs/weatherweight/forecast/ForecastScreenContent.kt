package io.github.tobyhs.weatherweight.forecast

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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

        Column(Modifier.verticalScroll(rememberScrollState())) {
            for (forecast in forecastResultSet.dailyForecasts) {
                DailyForecastCard(forecast)
            }

            HorizontalDivider(Modifier.padding(vertical = 8.dp))

            for (forecast in forecastResultSet.hourlyForecasts) {
                HourlyForecastCard(forecast)
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

@Preview
@Composable
private fun ForecastScreenContentPreview() {
    ForecastScreenContent(previewDataForecastResultSet)
}
