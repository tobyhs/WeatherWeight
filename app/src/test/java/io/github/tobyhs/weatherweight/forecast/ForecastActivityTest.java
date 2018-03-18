package io.github.tobyhs.weatherweight.forecast;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import io.github.tobyhs.weatherweight.R;
import io.github.tobyhs.weatherweight.test.WeatherResponseFactory;
import io.github.tobyhs.weatherweight.yahooweather.model.Channel;
import io.github.tobyhs.weatherweight.yahooweather.model.SingleForecast;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class ForecastActivityTest {
    private ForecastActivity activity = Robolectric.setupActivity(ForecastActivity.class);
    private ForecastPresenter presenter = activity.getPresenter();

    @Test
    public void onCreate() {
        assertThat(activity.locationSearch.isSubmitButtonEnabled(), is(true));
    }

    @Test
    public void onCreateWithNullSavedInstanceState() {
        verify(presenter).loadLastForecast();
    }

    @Test
    public void onCreateWithPresentSavedInstanceState() {
        ActivityController<ForecastActivity> controller = Robolectric.buildActivity(ForecastActivity.class);
        controller.create(new Bundle());

        presenter = controller.get().getPresenter();
        verify(presenter, never()).loadLastForecast();
    }

    @Test
    public void createPresenter() {
        assertThat(activity.createPresenter(), is(notNullValue()));
    }

    @Test
    public void getData() {
        Channel channel = WeatherResponseFactory.createChannel();
        when(presenter.getChannel()).thenReturn(channel);

        assertThat(activity.getData(), is(channel));
    }

    @Test
    public void setData() {
        Channel channel = WeatherResponseFactory.createChannel();
        activity.setData(channel);

        String location = channel.getLocation().toString();
        assertThat(activity.locationFoundView.getText().toString(), is(location));
        assertThat(activity.pubDateView.getText().toString(), is(channel.getItem().getPubDate()));

        activity.forecastRecyclerView.measure(0, 0);
        activity.forecastRecyclerView.layout(0, 0, 100, 10000);
        RecyclerView.LayoutManager layoutManager = activity.forecastRecyclerView.getLayoutManager();

        List<SingleForecast> forecasts = channel.getItem().getForecast();
        assertThat(layoutManager.getItemCount(), is(forecasts.size()));

        View view = layoutManager.findViewByPosition(0);
        SingleForecast singleForecast = forecasts.get(0);
        TextView dayView = view.findViewById(R.id.day);
        assertThat(dayView.getText().toString(), is(singleForecast.getDay()));
        TextView dateView = view.findViewById(R.id.date);
        assertThat(dateView.getText().toString(), is(singleForecast.getDate()));
        TextView lowView = view.findViewById(R.id.temperatureLow);
        assertThat(lowView.getText().toString(), is(singleForecast.getLow()));
        TextView highView = view.findViewById(R.id.temperatureHigh);
        assertThat(highView.getText().toString(), is(singleForecast.getHigh()));
        TextView descriptionView = view.findViewById(R.id.description);
        assertThat(descriptionView.getText().toString(), is(singleForecast.getText()));
    }

    @Test
    public void setDataWithNull() {
        // To check that a NullPointerException isn't thrown
        activity.setData(null);
    }

    @Test
    public void getErrorMessage() {
        Throwable throwable = new Throwable("test error");
        assertThat(activity.getErrorMessage(throwable, false), is(throwable.toString()));
    }

    @Test
    public void createViewState() {
        assertThat(activity.createViewState(), isA((Class) RetainingLceViewState.class));
    }

    @Test
    public void setLocationInputText() {
        String location = "Saved City, SC";
        activity.setLocationInputText(location);

        assertThat(activity.locationSearch.getQuery().toString(), is(location));
        assertThat(activity.locationSearch.hasFocus(), is(false));
        verify(presenter, never()).search(anyString());
    }

    @Test
    public void onQueryTextSubmit() {
        String location = "San Francisco, CA";
        activity.locationSearch.setQuery(location, true);
        verify(presenter).search(location);
    }

    @Test
    public void onQueryTextChange() {
        assertThat(activity.onQueryTextChange("i"), is(true));
    }

    @Test
    public void openAttributionUrl() {
        String attributionUrl = "https://www.yahoo.com/?from_weather_api";
        when(presenter.getAttributionUrl()).thenReturn(attributionUrl);

        activity.findViewById(R.id.poweredByYahooImage).performClick();

        Intent actual = shadowOf(activity).getNextStartedActivity();
        Intent expected = new Intent(Intent.ACTION_VIEW, Uri.parse(attributionUrl));
        assertThat(actual.filterEquals(expected), is(true));
    }
}
