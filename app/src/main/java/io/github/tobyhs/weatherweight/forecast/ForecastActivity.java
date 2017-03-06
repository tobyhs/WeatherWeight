package io.github.tobyhs.weatherweight.forecast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @BindView(R.id.locationInput) EditText locationInput;
    @BindView(R.id.locationFound) TextView locationFoundView;
    @BindView(R.id.timeFetched) TextView timeFetchedView;
    @BindView(R.id.forecastRecyclerView) RecyclerView forecastRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        forecastComponent = DaggerForecastComponent.builder()
                .appComponent(((App) getApplication()).getAppComponent())
                .build();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        setRetainInstance(true);
        ButterKnife.bind(this);
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

    /**
     * Opens the attribution URL
     */
    @OnClick(R.id.poweredByYahooImage)
    public void openAttributionUrl() {
        Uri uri = Uri.parse(getPresenter().getAttributionUrl());
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}
