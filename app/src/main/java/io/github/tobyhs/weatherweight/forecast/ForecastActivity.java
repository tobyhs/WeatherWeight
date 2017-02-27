package io.github.tobyhs.weatherweight.forecast;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.LinearLayout;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        forecastComponent = DaggerForecastComponent.builder()
                .appComponent(((App) getApplication()).getAppComponent())
                .build();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
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
        return null;
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
}
