package io.github.tobyhs.weatherweight.forecast;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.tobyhs.weatherweight.R;
import io.github.tobyhs.weatherweight.data.model.DailyForecast;
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet;
import io.github.tobyhs.weatherweight.databinding.ActivityForecastBinding;
import io.github.tobyhs.weatherweight.test.ForecastResultSetFactory;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.robolectric.Shadows.shadowOf;

@RunWith(AndroidJUnit4.class)
public class ForecastActivityTest {
    @Rule
    public ActivityScenarioRule<ForecastActivity> activityScenarioRule = new ActivityScenarioRule<>(ForecastActivity.class);

    @Test
    public void onCreate() {
        activityScenarioRule.getScenario().onActivity(activity ->
                assertThat(activity.binding.locationSearch.isSubmitButtonEnabled(), is(true))
        );
    }

    @Test
    public void onCreateWithNullSavedInstanceState() {
        activityScenarioRule.getScenario().onActivity(activity ->
                verify(activity.getPresenter()).loadLastForecast()
        );
    }

    @Test
    public void onCreateWithPresentSavedInstanceState() {
        ActivityScenario<ForecastActivity> scenario = activityScenarioRule.getScenario();
        scenario.onActivity(activity ->
                // Clearing so we can check loadLastForecast isn't called on the 2nd onCreate
                clearInvocations(activity.getPresenter())
        );

        scenario.recreate();
        scenario.onActivity(activity ->
                verify(activity.getPresenter(), never()).loadLastForecast()
        );
    }

    @Test
    public void createPresenter() {
        activityScenarioRule.getScenario().onActivity(activity ->
                assertThat(activity.createPresenter(), is(notNullValue()))
        );
    }

    @Test
    public void getData() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            ForecastResultSet forecastResultSet = ForecastResultSetFactory.create();
            when(activity.getPresenter().getForecastResultSet()).thenReturn(forecastResultSet);

            assertThat(activity.getData(), is(forecastResultSet));
        });
    }

    @Test
    public void setData() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            ActivityForecastBinding binding = activity.binding;
            binding.forecastSwipeContainer.setRefreshing(true);
            ForecastResultSet forecastResultSet = ForecastResultSetFactory.create();
            activity.setData(forecastResultSet);

            assertThat(binding.locationFound.getText().toString(), is("Oakland, CA, US"));
            assertThat(
                    binding.pubDate.getText().toString(),
                    is("Fri, 1 Feb 2019 12:00:00 GMT")
            );
            assertThat(binding.forecastSwipeContainer.isRefreshing(), is(false));

            RecyclerView recyclerView = binding.forecastRecyclerView;
            recyclerView.measure(0, 0);
            recyclerView.layout(0, 0, 0, 10000);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            assert layoutManager != null;

            List<DailyForecast> forecasts = forecastResultSet.getForecasts();
            assertThat(layoutManager.getItemCount(), is(forecasts.size()));

            View view = layoutManager.findViewByPosition(0);
            assert view != null;
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
        });
    }

    @Test
    public void setDataWithNull() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            // To check that a NullPointerException isn't thrown
            activity.setData(null);
        });
    }

    @Test
    public void getErrorMessage() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            Throwable throwable = new Throwable("test error");
            assertThat(activity.getErrorMessage(throwable, false), is(throwable.toString()));
        });
    }

    @Test
    public void createViewState() {
        activityScenarioRule.getScenario().onActivity(activity ->
                assertThat(activity.createViewState(), is(instanceOf(RetainingLceViewState.class)))
        );
    }

    @Test
    public void setLocationInputText() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            String location = "Saved City, SC";
            activity.setLocationInputText(location);

            assertThat(activity.binding.locationSearch.getQuery().toString(), is(location));
            assertThat(activity.binding.locationSearch.hasFocus(), is(false));
            verify(activity.getPresenter(), never()).search(anyString());
        });
    }

    @Test
    public void onQueryTextSubmit() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            String location = "San Francisco, CA";
            activity.binding.locationSearch.setQuery(location, true);
            verify(activity.getPresenter()).search(location);
        });
    }

    @Test
    public void onQueryTextChange() {
        activityScenarioRule.getScenario().onActivity(activity ->
                assertThat(activity.onQueryTextChange("i"), is(true))
        );
    }

    @Test
    public void onRefresh() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            String location = "Current";
            activity.binding.locationSearch.setQuery(location, false);
            activity.onRefresh();
            verify(activity.getPresenter()).search(location);
        });
    }

    @Test
    public void openAttributionUrl() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            activity.binding.accuweatherLogo.performClick();
            Intent actual = shadowOf(activity).getNextStartedActivity();
            Intent expected = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.accuweather.com")
            );
            assertThat(actual.filterEquals(expected), is(true));
        });
    }
}
