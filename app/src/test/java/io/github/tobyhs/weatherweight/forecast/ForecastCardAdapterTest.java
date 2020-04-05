package io.github.tobyhs.weatherweight.forecast;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.robolectric.RobolectricTestRunner;

import io.github.tobyhs.weatherweight.R;
import io.github.tobyhs.weatherweight.data.model.DailyForecast;
import io.github.tobyhs.weatherweight.test.ForecastResultSetFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class ForecastCardAdapterTest {
    private ForecastCardAdapter adapter = new ForecastCardAdapter();

    @Test
    public void getItemLayoutId() {
        assertThat(adapter.getItemLayoutId(0), is(R.layout.forecast_card));
    }

    @Test
    public void viewHolderBind() {
        Application app = ApplicationProvider.getApplicationContext();
        View view = LayoutInflater.from(app).inflate(R.layout.forecast_card, null);
        ForecastCardAdapter.ViewHolder viewHolder = adapter.getViewHolder(view, 0);
        DailyForecast forecast = ForecastResultSetFactory.createForecasts().get(0);

        viewHolder.bind(forecast);

        assertThat(viewHolder.binding.day.getText(), is("Fri"));
        assertThat(viewHolder.binding.date.getText(), is("Feb 1"));
        assertThat(viewHolder.binding.temperatureLow.getText(), is("60"));
        assertThat(viewHolder.binding.temperatureHigh.getText(), is("65"));
        assertThat(viewHolder.binding.description.getText(), is("Cloudy"));
    }
}
