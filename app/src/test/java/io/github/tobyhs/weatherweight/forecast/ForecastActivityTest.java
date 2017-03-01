package io.github.tobyhs.weatherweight.forecast;

import android.content.Intent;
import android.net.Uri;

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
import io.github.tobyhs.weatherweight.yahooweather.model.Channel;

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
    public void createPresenter() {
        assertThat(activity.createPresenter(), is(notNullValue()));
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
    public void tappingYahooImageOpensAttributionUrl() {
        String attributionUrl = "https://www.yahoo.com/?from_weather_api";
        when(presenter.getAttributionUrl()).thenReturn(attributionUrl);

        activity.findViewById(R.id.poweredByYahooImage).performClick();

        Intent actual = shadowActivity.getNextStartedActivity();
        Intent expected = new Intent(Intent.ACTION_VIEW, Uri.parse(attributionUrl));
        assertThat(actual.filterEquals(expected), is(true));
    }
}
