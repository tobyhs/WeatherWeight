package io.github.tobyhs.weatherweight.forecast;

import java.util.Locale;

import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.prokkypew.clearrecycleradapter.ClearRecyclerAdapter;
import com.prokkypew.clearrecycleradapter.ClearRecyclerViewHolder;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.TextStyle;

import io.github.tobyhs.weatherweight.R;
import io.github.tobyhs.weatherweight.data.model.DailyForecast;

/**
 * Recycler view adapter for the forecast card
 */
public class ForecastCardAdapter extends ClearRecyclerAdapter<DailyForecast> {
    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM d");

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.forecast_card;
    }

    @Override
    protected ClearRecyclerViewHolder<DailyForecast> getViewHolder(View view, int viewType) {
        return new ViewHolder(view);
    }

    /**
     * View holder for {@link ForecastCardAdapter}
     */
    static class ViewHolder extends ClearRecyclerViewHolder<DailyForecast> {
        @BindView(R.id.day) TextView day;
        @BindView(R.id.date) TextView date;
        @BindView(R.id.temperatureLow) TextView temperatureLow;
        @BindView(R.id.temperatureHigh) TextView temperatureHigh;
        @BindView(R.id.description) TextView description;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void bind(DailyForecast forecast) {
            day.setText(forecast.getDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()));
            date.setText(forecast.getDate().format(DATE_FORMATTER));
            temperatureLow.setText(Integer.toString(forecast.getLow()));
            temperatureHigh.setText(Integer.toString(forecast.getHigh()));
            description.setText(forecast.getText());
        }
    }
}
