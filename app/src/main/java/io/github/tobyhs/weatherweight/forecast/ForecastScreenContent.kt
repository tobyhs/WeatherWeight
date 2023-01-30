package io.github.tobyhs.weatherweight.forecast

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview

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

        for (forecast in forecastResultSet.forecasts) {
            ForecastCard(forecast)
        }
    }
}

internal val previewDataForecastResultSet = ForecastResultSet(
    location = "Some City, ST",
    publicationTime = ZonedDateTime.of(2023, 1, 15, 12, 30, 0, 0, ZoneId.systemDefault()),
    forecasts = previewDataForecasts,
)

@Preview
@Composable
private fun ForecastScreenContentPreview() {
    ForecastScreenContent(previewDataForecastResultSet)
}
