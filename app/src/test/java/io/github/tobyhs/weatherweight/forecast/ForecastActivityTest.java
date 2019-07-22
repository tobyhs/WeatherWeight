package io.github.tobyhs.weatherweight.forecast;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import io.github.tobyhs.weatherweight.R;
import io.github.tobyhs.weatherweight.data.model.DailyForecast;
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet;
import io.github.tobyhs.weatherweight.test.ForecastResultSetFactory;

import static org.hamcrest.CoreMatchers.is;
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
        ForecastResultSet forecastResultSet = ForecastResultSetFactory.create();
        when(presenter.getForecastResultSet()).thenReturn(forecastResultSet);

        assertThat(activity.getData(), is(forecastResultSet));
    }

    @Test
    public void setData() {
        activity.forecastSwipeContainer.setRefreshing(true);
        ForecastResultSet forecastResultSet = ForecastResultSetFactory.create();
        activity.setData(forecastResultSet);

        assertThat(activity.locationFoundView.getText().toString(), is("Oakland, CA, US"));
        assertThat(activity.pubDateView.getText().toString(), is("Fri, 1 Feb 2019 12:00:00 GMT"));
        assertThat(activity.forecastSwipeContainer.isRefreshing(), is(false));

        activity.forecastRecyclerView.measure(0, 0);
        activity.forecastRecyclerView.layout(0, 0, 00, 10000);
        RecyclerView.LayoutManager layoutManager = activity.forecastRecyclerView.getLayoutManager();

        List<DailyForecast> forecasts = forecastResultSet.getForecasts();
        assertThat(layoutManager.getItemCount(), is(forecasts.size()));

        View view = layoutManager.findViewByPosition(0);
        TextView dayView = view.findViewById(R.id.day);
        assertThat(dayView.getText().toString(), is("Fri"));
        TextView dateView = view.findViewById(R.id.date);
        assertThat(dateView.getText().toString(), is("Feb 1"));
        TextView lowView = view.findViewById(R.id.temperatureLow);
        assertThat(lowView.getText().toString(), is("60"));
        TextView highView = view.findViewById(R.id.temperatureHigh);
        assertThat(highView.getText().toString(), is("65"));
        TextView descriptionView = view.findViewById(R.id.description);
        assertThat(descriptionView.getText().toString(), is("Cloudy"));
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
        assertThat(activity.createViewState() instanceof RetainingLceViewState, is(true));
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
    public void onRefresh() {
        String location = "Current";
        activity.locationSearch.setQuery(location, false);
        activity.onRefresh();
        verify(presenter).search(location);
    }

    @Test
    public void openAttributionUrl() {
        activity.findViewById(R.id.accuweather_logo).performClick();
        Intent actual = shadowOf(activity).getNextStartedActivity();
        Intent expected = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.accuweather.com"));
        assertThat(actual.filterEquals(expected), is(true));
    }
}
