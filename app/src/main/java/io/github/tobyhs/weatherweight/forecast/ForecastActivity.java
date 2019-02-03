package io.github.tobyhs.weatherweight.forecast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.MvpLceViewStateActivity;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState;

import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import dagger.Lazy;
import dagger.android.AndroidInjection;

import io.github.tobyhs.weatherweight.R;
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet;

/**
 * Main activity to enter a location to retrieve a weather forecast
 */
public class ForecastActivity
        extends MvpLceViewStateActivity<LinearLayout, ForecastResultSet, ForecastContract.View, ForecastPresenter>
        implements ForecastContract.View, SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {
    private static Uri ACCUWEATHER_URI = Uri.parse("https://www.accuweather.com");

    @Inject
    protected Lazy<ForecastPresenter> lazyPresenter;

    private ForecastCardAdapter forecastCardAdapter;

    @BindView(R.id.locationSearch) SearchView locationSearch;
    @BindView(R.id.locationFound) TextView locationFoundView;
    @BindView(R.id.pubDate) TextView pubDateView;
    @BindView(R.id.forecastSwipeContainer) SwipeRefreshLayout forecastSwipeContainer;
    @BindView(R.id.forecastRecyclerView) RecyclerView forecastRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        ButterKnife.bind(this);

        locationSearch.setOnQueryTextListener(this);
        locationSearch.setSubmitButtonEnabled(true);

        forecastCardAdapter = new ForecastCardAdapter();
        forecastRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        forecastRecyclerView.setAdapter(forecastCardAdapter);

        forecastSwipeContainer.setOnRefreshListener(this);

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

        locationFoundView.setText(forecastResultSet.getLocation());
        ZonedDateTime pubDate = forecastResultSet.getPublicationTime();
        pubDateView.setText(pubDate.format(DateTimeFormatter.RFC_1123_DATE_TIME));
        forecastSwipeContainer.setRefreshing(false);
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
        locationSearch.setQuery(location, false);
        // I don't want locationSearch to have focus after loading the last forecast because it
        // will bring up the keyboard, so I'll force focus on another view
        forecastRecyclerView.requestFocus();
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
        getPresenter().search(locationSearch.getQuery().toString());
    }

    /**
     * Opens the attribution URL
     */
    @OnClick(R.id.accuweather_logo)
    public void openAttributionUrl() {
        startActivity(new Intent(Intent.ACTION_VIEW, ACCUWEATHER_URI));
    }
}
