package io.github.tobyhs.weatherweight.forecast;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.util.ActivityController;

import io.github.tobyhs.weatherweight.R;
import io.github.tobyhs.weatherweight.test.BaseTestCase;
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
public class ForecastActivityTest extends BaseTestCase {
    private ForecastPresenter presenter;
    private ForecastActivity activity = Robolectric.setupActivity(ForecastActivity.class);

    @Before
    public void setup() {
        presenter = activity.getPresenter();
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
    public void isRetainInstance() {
        assertThat(activity.isRetainInstance(), is(true));
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
        TextView dateView = (TextView) view.findViewById(R.id.date);
        assertThat(dateView.getText().toString(), is(singleForecast.getDate()));
        TextView lowView = (TextView) view.findViewById(R.id.temperatureLow);
        assertThat(lowView.getText().toString(), is(singleForecast.getLow()));
        TextView highView = (TextView) view.findViewById(R.id.temperatureHigh);
        assertThat(highView.getText().toString(), is(singleForecast.getHigh()));
        TextView descriptionView = (TextView) view.findViewById(R.id.description);
        assertThat(descriptionView.getText().toString(), is(singleForecast.getText()));
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
    public void submitLocation() {
        String location = "New York, NY";
        activity.locationInput.setText(location);

        activity.locationInput.onEditorAction(EditorInfo.IME_ACTION_GO);
        verify(presenter).search(location);
    }

    @Test
    public void submitLocationWithIrrelevantActionId() {
        String location = "blah";
        activity.locationInput.setText(location);

        assertThat(activity.submitLocation(EditorInfo.IME_ACTION_PREVIOUS), is(false));
        verify(presenter, never()).search(anyString());
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
