package io.github.tobyhs.weatherweight.forecast;

import android.view.View;
import android.widget.TextView;

import org.junit.Test;

import io.github.tobyhs.weatherweight.R;
import io.github.tobyhs.weatherweight.data.model.DailyForecast;
import io.github.tobyhs.weatherweight.test.ForecastResultSetFactory;

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
        DailyForecast forecast = ForecastResultSetFactory.createForecasts().get(0);

        viewHolder.bind(forecast);

        verify(viewHolder.day).setText("Fri");
        verify(viewHolder.date).setText("Feb 1");
        verify(viewHolder.temperatureLow).setText("60");
        verify(viewHolder.temperatureHigh).setText("65");
        verify(viewHolder.description).setText("Cloudy");
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
