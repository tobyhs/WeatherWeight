package io.github.tobyhs.weatherweight.forecast;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;

import io.github.tobyhs.weatherweight.R;
import io.github.tobyhs.weatherweight.test.BaseTestCase;
import io.github.tobyhs.weatherweight.test.WeatherResponseFactory;
import io.github.tobyhs.weatherweight.yahooweather.model.Channel;
import io.github.tobyhs.weatherweight.yahooweather.model.SingleForecast;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class ForecastActivityTest extends BaseTestCase {
    @Mock private ForecastPresenter presenter;
    private ForecastActivity activity = Robolectric.setupActivity(ForecastActivity.class);
    private ShadowActivity shadowActivity;

    @Before
    public void setup() {
        shadowActivity = shadowOf(activity);
        activity.setPresenter(presenter);
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
        LceViewState<Channel, ForecastContract.View> viewState = activity.createViewState();
        assertThat(viewState.getClass(), is(equalTo((Class) RetainingLceViewState.class)));
    }

    @Test
    public void openAttributionUrl() {
        String attributionUrl = "https://www.yahoo.com/?from_weather_api";
        when(presenter.getAttributionUrl()).thenReturn(attributionUrl);

        activity.findViewById(R.id.poweredByYahooImage).performClick();

        Intent actual = shadowActivity.getNextStartedActivity();
        Intent expected = new Intent(Intent.ACTION_VIEW, Uri.parse(attributionUrl));
        assertThat(actual.filterEquals(expected), is(true));
    }
}
