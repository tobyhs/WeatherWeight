package io.github.tobyhs.weatherweight.forecast;

import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.prokkypew.clearrecycleradapter.ClearRecyclerAdapter;
import com.prokkypew.clearrecycleradapter.ClearRecyclerViewHolder;

import io.github.tobyhs.weatherweight.R;
import io.github.tobyhs.weatherweight.yahooweather.model.SingleForecast;

/**
 * Recycler view adapter for the forecast card
 */
public class ForecastCardAdapter extends ClearRecyclerAdapter<SingleForecast> {
    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.forecast_card;
    }

    @Override
    protected ClearRecyclerViewHolder<SingleForecast> getViewHolder(View view, int viewType) {
        return new ViewHolder(view);
    }

    /**
     * View holder for {@link ForecastCardAdapter}
     */
    static class ViewHolder extends ClearRecyclerViewHolder<SingleForecast> {
        @BindView(R.id.date) TextView date;
        @BindView(R.id.temperatureLow) TextView temperatureLow;
        @BindView(R.id.temperatureHigh) TextView temperatureHigh;
        @BindView(R.id.description) TextView description;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void bind(SingleForecast forecast) {
            date.setText(forecast.getDate());
            temperatureLow.setText(forecast.getLow());
            temperatureHigh.setText(forecast.getHigh());
            description.setText(forecast.getText());
        }
    }
}
