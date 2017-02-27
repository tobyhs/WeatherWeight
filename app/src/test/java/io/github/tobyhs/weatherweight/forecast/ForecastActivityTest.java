package io.github.tobyhs.weatherweight.forecast;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import io.github.tobyhs.weatherweight.yahooweather.model.Channel;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class ForecastActivityTest {
    private ForecastActivity activity = Robolectric.setupActivity(ForecastActivity.class);

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
}
