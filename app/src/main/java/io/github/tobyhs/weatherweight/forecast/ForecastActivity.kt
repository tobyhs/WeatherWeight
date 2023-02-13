package io.github.tobyhs.weatherweight.forecast

import android.os.Bundle

import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import dagger.hilt.android.AndroidEntryPoint

import io.github.tobyhs.weatherweight.di.ViewModelFactoryProducer
import io.github.tobyhs.weatherweight.theme.AppTheme

import javax.inject.Inject

/**
 * Main activity to enter a location to retrieve a weather forecast
 */
@AndroidEntryPoint
class ForecastActivity : AppCompatActivity() {
    @set:Inject var viewModelFactoryProducer: ViewModelFactoryProducer? = null
    private lateinit var viewModel: ForecastViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModels<ForecastViewModel>(factoryProducer = viewModelFactoryProducer).value
        if (savedInstanceState == null) {
            viewModel.loadLastForecast()
        }

        setContent {
            AppTheme {
                ForecastScreen(viewModel)
            }
        }
    }
}
