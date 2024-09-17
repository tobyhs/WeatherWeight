package io.github.tobyhs.weatherweight.forecast

import android.content.Intent
import android.net.Uri

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import io.github.tobyhs.weatherweight.R
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet
import io.github.tobyhs.weatherweight.ui.LoadState

/**
 * Main composable for [ForecastActivity]
 *
 * @param viewModel
 */
@Composable
fun ForecastScreen(viewModel: ForecastViewModel) {
    val forecastState by viewModel.forecastState.collectAsStateWithLifecycle()
    val locationInput by viewModel.locationInput.collectAsStateWithLifecycle()

    ForecastScreen(
        forecastState = forecastState,
        locationInput = locationInput,
        onLocationInputChange = { viewModel.locationInput.value = it },
        onSubmit = viewModel::search,
    )
}

@Composable
private fun ForecastScreen(
    forecastState: LoadState<ForecastResultSet>?,
    locationInput: String = "",
    onLocationInputChange: (String) -> Unit = {},
    onSubmit: () -> Unit = {},
) {
    Column(
        Modifier
            .padding(all = dimensionResource(R.dimen.forecast_screen_padding))
            .verticalScroll(rememberScrollState())
    ) {
        val inputContentDescription = stringResource(R.string.locationSearchHint)
        OutlinedTextField(
            value = locationInput,
            onValueChange = { onLocationInputChange(it) },
            label = { Text(stringResource(R.string.location)) },
            singleLine = true,
            leadingIcon = {
                IconButton(onClick = onSubmit) {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = stringResource(R.string.submitLocation)
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSubmit() }),
            modifier = Modifier.semantics { contentDescription = inputContentDescription }
        )
        Spacer(Modifier.height(8.dp))

        Box(Modifier.fillMaxWidth()) {
            when (forecastState) {
                is LoadState.Loading -> {
                    CircularProgressIndicator(
                        Modifier.align(Alignment.TopCenter).testTag("loading")
                    )
                }
                is LoadState.Content -> ForecastScreenContent(forecastState.content)
                is LoadState.Error -> Text(
                    forecastState.error.message.toString(),
                    Modifier.testTag("error")
                )
                null -> {}
            }
        }

        Spacer(Modifier.height(8.dp))
        Text(stringResource(R.string.powered_by))
        val context = LocalContext.current
        Image(
            painterResource(R.drawable.accuweather),
            contentDescription = stringResource(R.string.powered_by_accuweather),
            modifier = Modifier.clickable {
                context.startActivity(Intent(Intent.ACTION_VIEW, ACCUWEATHER_URI))
            },
        )
    }
}

private val ACCUWEATHER_URI = Uri.parse("https://www.accuweather.com")

@Preview
@Composable
private fun ForecastScreenLoadingPreview() {
    ForecastScreen(LoadState.Loading(), locationInput = "Searching, CA")
}

@Preview
@Composable
private fun ForecastScreenContentStatePreview() {
    ForecastScreen(LoadState.Content(previewDataForecastResultSet), locationInput = "Some City, ST")
}

@Preview
@Composable
private fun ForecastScreenErrorPreview() {
    val error = Error("An error occurred")
    ForecastScreen(LoadState.Error(error), locationInput = "Nonexistent City")
}
