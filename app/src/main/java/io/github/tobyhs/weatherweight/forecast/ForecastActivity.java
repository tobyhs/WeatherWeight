package io.github.tobyhs.weatherweight.forecast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.MvpLceViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import io.github.tobyhs.weatherweight.App;
import io.github.tobyhs.weatherweight.R;
import io.github.tobyhs.weatherweight.yahooweather.model.Channel;

/**
 * Main activity to enter a location to retrieve a weather forecast
 */
public class ForecastActivity
        extends MvpLceViewStateActivity<LinearLayout, Channel, ForecastContract.View, ForecastPresenter>
        implements ForecastContract.View {
    private ForecastComponent forecastComponent;
    private ForecastCardAdapter forecastCardAdapter;

    @BindView(R.id.locationInput) EditText locationInput;
    @BindView(R.id.locationFound) TextView locationFoundView;
    @BindView(R.id.pubDate) TextView pubDateView;
    @BindView(R.id.forecastRecyclerView) RecyclerView forecastRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        forecastComponent = ((App) getApplication()).createForecastComponent();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        setRetainInstance(true);
        ButterKnife.bind(this);

        forecastCardAdapter = new ForecastCardAdapter();
        forecastRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        forecastRecyclerView.setAdapter(forecastCardAdapter);
    }

    @Override
    @NonNull
    public ForecastPresenter createPresenter() {
        return forecastComponent.forecastPresenter();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
    }

    @Override
    public Channel getData() {
        return getPresenter().getChannel();
    }

    @Override
    public void setData(Channel channel) {
        locationFoundView.setText(channel.getLocation().toString());
        pubDateView.setText(channel.getItem().getPubDate());
        forecastCardAdapter.set(channel.getItem().getForecast());
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.toString();
    }

    @Override
    @NonNull
    public LceViewState<Channel, ForecastContract.View> createViewState() {
        return new RetainingLceViewState<>();
    }

    @OnEditorAction(R.id.locationInput)
    public boolean submitLocation(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            getPresenter().search(locationInput.getText().toString());
            return true;
        }
        return false;
    }

    /**
     * Opens the attribution URL
     */
    @OnClick(R.id.poweredByYahooImage)
    public void openAttributionUrl() {
        Uri uri = Uri.parse(getPresenter().getAttributionUrl());
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}
