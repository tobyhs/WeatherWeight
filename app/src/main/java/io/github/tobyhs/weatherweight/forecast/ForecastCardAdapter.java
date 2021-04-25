package io.github.tobyhs.weatherweight.forecast;

import java.util.Locale;

import android.view.View;

import com.prokkypew.clearrecycleradapter.ClearRecyclerAdapter;
import com.prokkypew.clearrecycleradapter.ClearRecyclerViewHolder;

import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.TextStyle;

import io.github.tobyhs.weatherweight.R;
import io.github.tobyhs.weatherweight.data.model.DailyForecast;
import io.github.tobyhs.weatherweight.databinding.ForecastCardBinding;

/**
 * Recycler view adapter for the forecast card
 */
public class ForecastCardAdapter extends ClearRecyclerAdapter<DailyForecast> {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM d");

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.forecast_card;
    }

    @Override
    protected ViewHolder getViewHolder(View view, int viewType) {
        return new ViewHolder(view);
    }

    /**
     * View holder for {@link ForecastCardAdapter}
     */
    static class ViewHolder extends ClearRecyclerViewHolder<DailyForecast> {
        ForecastCardBinding binding;

        /**
         * @param view the forecast card view to bind to
         */
        ViewHolder(View view) {
            super(view);
            binding = ForecastCardBinding.bind(view);
        }

        @Override
        public void bind(DailyForecast forecast) {
            String dayText = forecast.getDate().getDayOfWeek().getDisplayName(
                    TextStyle.SHORT, Locale.getDefault()
            );
            binding.day.setText(dayText);
            binding.date.setText(forecast.getDate().format(DATE_FORMATTER));
            Locale locale = Locale.getDefault();
            binding.temperatureLow.setText(String.format(locale, "%d", forecast.getLow()));
            binding.temperatureHigh.setText(String.format(locale, "%d", forecast.getHigh()));
            binding.description.setText(forecast.getText());
        }
    }
}
