package io.github.tobyhs.weatherweight.forecast

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

import io.github.tobyhs.weatherweight.R
import io.github.tobyhs.weatherweight.data.model.HourlyForecast
import io.github.tobyhs.weatherweight.ui.LocalClock

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun HourlyForecastCard(forecast: HourlyForecast) {
    Card(Modifier.fillMaxWidth().padding(dimensionResource(R.dimen.forecast_card_padding))) {
        FlowRow(
            Modifier.padding(
                horizontal = dimensionResource(R.dimen.forecast_card_row_horizontal_padding)
            )
        ) {
            Text(
                forecast.dateTime.withZoneSameInstant(LocalClock.current.zone).format(HOUR_FORMATTER),
                modifier = Modifier
                    .width(dimensionResource(R.dimen.hourly_forecast_card_hour_width))
                    .testTag("hour"),
            )

            Text(
                forecast.temperature.toString(),
                modifier = Modifier
                    .width(dimensionResource(R.dimen.forecast_card_temperature_width))
                    .testTag("hourlyTemperature"),
                textAlign = TextAlign.Right,
            )

            Text(
                stringResource(R.string.percent, forecast.precipitationProbability),
                modifier = Modifier
                    .width(dimensionResource(R.dimen.forecast_card_precipitation_probability_width))
                    .testTag("hourlyPrecipitationProbability"),
                textAlign = TextAlign.Right,
            )

            Spacer(Modifier.width(dimensionResource(R.dimen.forecast_card_description_spacing)))

            Text(
                forecast.text,
                modifier = Modifier
                    .defaultMinSize(minWidth = dimensionResource(R.dimen.forecast_card_description_min_width))
                    .testTag("hourlyForecastDescription")
            )
        }
    }
}

private val HOUR_FORMATTER = DateTimeFormatter.ofPattern("HH:mm")

internal val previewDataHourlyForecasts = listOf(
    HourlyForecast(
        dateTime = ZonedDateTime.parse("2019-02-01T11:00:00Z"),
        text = "Mostly clear",
        temperature = 63,
        precipitationProbability = 5,
    ),
    HourlyForecast(
        dateTime = ZonedDateTime.parse("2019-02-01T12:00:00Z"),
        text = "Cloudy",
        temperature = 67,
        precipitationProbability = 10,
    )
)

@Preview
@Composable
fun HourlyForecastCardPreview() {
    HourlyForecastCard(previewDataHourlyForecasts[0])
}
