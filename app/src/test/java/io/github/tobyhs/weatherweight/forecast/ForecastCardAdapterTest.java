package io.github.tobyhs.weatherweight.forecast;

import android.view.View;
import android.widget.TextView;

import org.junit.Test;

import io.github.tobyhs.weatherweight.R;
import io.github.tobyhs.weatherweight.yahooweather.model.SingleForecast;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ForecastCardAdapterTest {
    private ForecastCardAdapter adapter = new ForecastCardAdapter();

    @Test
    public void getItemLayoutId() {
        assertThat(adapter.getItemLayoutId(0), is(R.layout.forecast_card));
    }

    @Test
    public void viewHolderBind() {
        View view = mockView();
        ForecastCardAdapter.ViewHolder viewHolder;
        viewHolder = (ForecastCardAdapter.ViewHolder) adapter.getViewHolder(view, 0);

        String date = "26 Feb 2017";
        String day = "Sun";
        String low = "70";
        String high = "79";
        String description = "Rainy";
        SingleForecast forecast = SingleForecast.builder()
                .setDate(date)
                .setDay(day)
                .setLow(low)
                .setHigh(high)
                .setText(description)
                .build();

        viewHolder.bind(forecast);

        verify(viewHolder.day).setText(day);
        verify(viewHolder.date).setText(date);
        verify(viewHolder.temperatureLow).setText(low);
        verify(viewHolder.temperatureHigh).setText(high);
        verify(viewHolder.description).setText(description);
    }

    private static View mockView() {
        View view = mock(View.class);
        int[] viewIds = {
                R.id.day,
                R.id.date,
                R.id.temperatureLow,
                R.id.temperatureHigh,
                R.id.description,
        };
        for (int viewId : viewIds) {
            when(view.findViewById(viewId)).thenReturn(mock(TextView.class));
        }
        return view;
    }
}
