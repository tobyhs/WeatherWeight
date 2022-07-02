package io.github.tobyhs.weatherweight.forecast

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.SearchView

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener

import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter

import dagger.hilt.android.AndroidEntryPoint

import io.github.tobyhs.weatherweight.databinding.ActivityForecastBinding
import io.github.tobyhs.weatherweight.di.ViewModelFactoryProducer
import io.github.tobyhs.weatherweight.ui.LoadState

import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Main activity to enter a location to retrieve a weather forecast
 */
@AndroidEntryPoint
class ForecastActivity : AppCompatActivity(), SearchView.OnQueryTextListener, OnRefreshListener {
    private lateinit var binding: ActivityForecastBinding
    @set:Inject var viewModelFactoryProducer: ViewModelFactoryProducer? = null
    private lateinit var viewModel: ForecastViewModel
    private lateinit var forecastItemAdapter: ItemAdapter<ForecastCardItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForecastBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = viewModels<ForecastViewModel>(factoryProducer = viewModelFactoryProducer).value
        initializeViews()
        setupObservers()

        if (savedInstanceState == null) {
            viewModel.loadLastForecast()
        }
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        viewModel.search()
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        if (newText != viewModel.locationInput.value) {
            viewModel.locationInput.value = newText
        }
        return true
    }

    override fun onRefresh() {
        viewModel.search()
    }

    /**
     * Sets up views in the activity layout
     */
    private fun initializeViews() {
        binding.locationSearch.setOnQueryTextListener(this)
        binding.locationSearch.isSubmitButtonEnabled = true

        forecastItemAdapter = ItemAdapter()
        binding.forecastRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.forecastRecyclerView.adapter = FastAdapter.with(forecastItemAdapter)

        binding.forecastSwipeContainer.setOnRefreshListener(this)
        binding.accuweatherLogo.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, ACCUWEATHER_URI))
        }
    }

    /**
     * Registers LiveData observers
     */
    private fun setupObservers() {
        viewModel.locationInput.observe(this) { location ->
            binding.locationSearch.setQuery(location, false)
        }

        viewModel.forecastState.observe(this) { forecastState ->
            if (forecastState is LoadState.Content) {
                val forecastResultSet = forecastState.content
                binding.locationFound.text = forecastResultSet.location
                val pubDate = forecastResultSet.publicationTime
                binding.pubDate.text = pubDate.format(DateTimeFormatter.RFC_1123_DATE_TIME)
                binding.forecastSwipeContainer.isRefreshing = false
                forecastItemAdapter.set(forecastResultSet.forecasts.map { dailyForecast ->
                    ForecastCardItem(dailyForecast)
                })
            } else if (forecastState is LoadState.Error) {
                binding.errorView.text = forecastState.error.message
            }

            binding.loadingView.isVisible = forecastState is LoadState.Loading
            binding.contentView.isVisible = forecastState is LoadState.Content
            binding.errorView.isVisible = forecastState is LoadState.Error
        }
    }

    companion object {
        private val ACCUWEATHER_URI = Uri.parse("https://www.accuweather.com")
    }
}
