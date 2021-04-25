package io.github.tobyhs.weatherweight.forecast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import javax.inject.Inject;

import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.MvpLceViewStateActivity;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState;

import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import dagger.Lazy;
import dagger.android.AndroidInjection;

import io.github.tobyhs.weatherweight.data.model.ForecastResultSet;
import io.github.tobyhs.weatherweight.databinding.ActivityForecastBinding;

/**
 * Main activity to enter a location to retrieve a weather forecast
 */
public class ForecastActivity
        extends MvpLceViewStateActivity<LinearLayout, ForecastResultSet, ForecastContract.View, ForecastPresenter>
        implements ForecastContract.View, SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {
    private static final Uri ACCUWEATHER_URI = Uri.parse("https://www.accuweather.com");

    @Inject
    protected Lazy<ForecastPresenter> lazyPresenter;

    ActivityForecastBinding binding;
    private ForecastCardAdapter forecastCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        binding = ActivityForecastBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initializeViews();

        if (savedInstanceState == null) {
            getPresenter().loadLastForecast();
        }
    }

    @Override
    @NonNull
    public ForecastPresenter createPresenter() {
        return lazyPresenter.get();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
    }

    @Override
    public ForecastResultSet getData() {
        return getPresenter().getForecastResultSet();
    }

    @Override
    public void setData(ForecastResultSet forecastResultSet) {
        if (forecastResultSet == null) {
            return;
        }

        binding.locationFound.setText(forecastResultSet.getLocation());
        ZonedDateTime pubDate = forecastResultSet.getPublicationTime();
        binding.pubDate.setText(pubDate.format(DateTimeFormatter.RFC_1123_DATE_TIME));
        binding.forecastSwipeContainer.setRefreshing(false);
        forecastCardAdapter.set(forecastResultSet.getForecasts());
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.toString();
    }

    @Override
    @NonNull
    public LceViewState<ForecastResultSet, ForecastContract.View> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public void setLocationInputText(String location) {
        binding.locationSearch.setQuery(location, false);
        // I don't want locationSearch to have focus after loading the last forecast because it
        // will bring up the keyboard, so I'll force focus on another view
        binding.forecastRecyclerView.requestFocus();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        getPresenter().search(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    @Override
    public void onRefresh() {
        getPresenter().search(binding.locationSearch.getQuery().toString());
    }

    /**
     * Sets up views in the activity layout
     */
    private void initializeViews() {
        binding.locationSearch.setOnQueryTextListener(this);
        binding.locationSearch.setSubmitButtonEnabled(true);

        forecastCardAdapter = new ForecastCardAdapter();
        binding.forecastRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.forecastRecyclerView.setAdapter(forecastCardAdapter);

        binding.forecastSwipeContainer.setOnRefreshListener(this);

        binding.accuweatherLogo.setOnClickListener(view ->
            startActivity(new Intent(Intent.ACTION_VIEW, ACCUWEATHER_URI))
        );
    }
}
