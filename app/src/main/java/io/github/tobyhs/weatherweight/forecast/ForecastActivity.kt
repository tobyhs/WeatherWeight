package io.github.tobyhs.weatherweight.forecast

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener

import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.MvpLceViewStateActivity
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState

import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter

import dagger.Lazy
import dagger.hilt.android.AndroidEntryPoint

import io.github.tobyhs.weatherweight.data.model.ForecastResultSet
import io.github.tobyhs.weatherweight.databinding.ActivityForecastBinding

import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Main activity to enter a location to retrieve a weather forecast
 */
@AndroidEntryPoint
class ForecastActivity :
    MvpLceViewStateActivity<LinearLayout, ForecastResultSet, ForecastContract.View, ForecastPresenter>(),
    ForecastContract.View, SearchView.OnQueryTextListener, OnRefreshListener {
    @Inject
    lateinit var lazyPresenter: Lazy<ForecastPresenter>

    lateinit var binding: ActivityForecastBinding
    private lateinit var forecastItemAdapter: ItemAdapter<ForecastCardItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForecastBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeViews()

        if (savedInstanceState == null) {
            getPresenter().loadLastForecast()
        }
    }

    override fun createPresenter(): ForecastPresenter {
        return lazyPresenter.get()
    }

    override fun loadData(pullToRefresh: Boolean) {
    }

    override fun getData(): ForecastResultSet? {
        return getPresenter().forecastResultSet
    }

    override fun setData(forecastResultSet: ForecastResultSet?) {
        if (forecastResultSet == null) {
            return
        }

        binding.locationFound.text = forecastResultSet.location
        val pubDate = forecastResultSet.publicationTime
        binding.pubDate.text = pubDate.format(DateTimeFormatter.RFC_1123_DATE_TIME)
        binding.forecastSwipeContainer.isRefreshing = false
        forecastItemAdapter.set(forecastResultSet.forecasts.map { dailyForecast ->
            ForecastCardItem(dailyForecast)
        })
    }

    public override fun getErrorMessage(e: Throwable, pullToRefresh: Boolean): String {
        return e.toString()
    }

    override fun createViewState(): LceViewState<ForecastResultSet, ForecastContract.View> {
        return RetainingLceViewState()
    }

    override fun setLocationInputText(location: String) {
        binding.locationSearch.setQuery(location, false)
        // I don't want locationSearch to have focus after loading the last forecast because it
        // will bring up the keyboard, so I'll force focus on another view
        binding.forecastRecyclerView.requestFocus()
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        getPresenter().search(query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        return true
    }

    override fun onRefresh() {
        getPresenter().search(binding.locationSearch.query.toString())
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

    companion object {
        private val ACCUWEATHER_URI = Uri.parse("https://www.accuweather.com")
    }
}
