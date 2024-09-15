package io.github.tobyhs.weatherweight.forecast

import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import io.github.tobyhs.weatherweight.data.model.DailyForecast

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

/**
 * Displays forecast data for one day
 *
 * @param forecast the forecast to display data for
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DailyForecastCard(forecast: DailyForecast) {
    val locale = Locale.getDefault()

    Card(Modifier.fillMaxWidth().padding(dimensionResource(R.dimen.forecast_card_padding))) {
        FlowRow(
            Modifier.padding(
                horizontal = dimensionResource(R.dimen.forecast_card_row_horizontal_padding)
            )
        ) {
            Text(
                forecast.date.dayOfWeek.getDisplayName(TextStyle.SHORT, locale),
                modifier = Modifier
                    .width(dimensionResource(R.dimen.forecast_card_day_width))
                    .testTag("dayOfWeek"),
            )

            Text(
                forecast.date.format(DATE_FORMATTER),
                modifier = Modifier
                    .width(dimensionResource(R.dimen.forecast_card_date_width))
                    .testTag("date"),
            )

            Text(
                forecast.low.toString(),
                modifier = Modifier
                    .width(dimensionResource(R.dimen.forecast_card_temperature_width))
                    .testTag("lowTemperature"),
                textAlign = TextAlign.Right,
            )

            Text(
                forecast.high.toString(),
                modifier = Modifier
                    .width(dimensionResource(R.dimen.forecast_card_temperature_width))
                    .testTag("highTemperature"),
                textAlign = TextAlign.Right,
            )

            Text(
                stringResource(R.string.percent, forecast.precipitationProbability),
                modifier = Modifier
                    .width(dimensionResource(R.dimen.forecast_card_precipitation_probability_width))
                    .testTag("precipitationProbability"),
                textAlign = TextAlign.Right,
            )

            Spacer(Modifier.width(dimensionResource(R.dimen.forecast_card_description_spacing)))

            Text(
                forecast.text,
                modifier = Modifier
                    .defaultMinSize(minWidth = dimensionResource(R.dimen.forecast_card_description_min_width))
                    .testTag("forecastDescription")
            )
        }
    }
}

internal val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d")

internal val previewDataDailyForecasts = listOf(
    DailyForecast(
        date = LocalDate.parse("2023-01-15"),
        low = 45,
        high = 60,
        text = "Partly Sunny / Intermittent Clouds",
        precipitationProbability = 40,
    ),
    DailyForecast(
        date = LocalDate.parse("2023-01-16"),
        low = 40,
        high = 55,
        text = "Rain",
        precipitationProbability = 80,
    ),
    DailyForecast(
        date = LocalDate.parse("2023-01-17"),
        low = 50,
        high = 58,
        text = "Thunderstorms / Rain",
        precipitationProbability = 100,
    ),
    DailyForecast(
        date = LocalDate.parse("2023-01-18"),
        low = 60,
        high = 70,
        text = "Mostly Clear",
        precipitationProbability = 7,
    ),
    DailyForecast(
        date = LocalDate.parse("2023-01-19"),
        low = 100,
        high = 105,
        text = "Sunny",
        precipitationProbability = 0,
    ),
)

@Preview
@Composable
private fun DailyForecastCardPreview() {
    DailyForecastCard(previewDataDailyForecasts[0])
}
