package io.github.tobyhs.weatherweight.data.model;

import com.google.auto.value.AutoValue;
import me.tatarka.gsonvalue.annotations.GsonBuilder;
import org.threeten.bp.LocalDate;

/**
 * A forecast for a particular day
 */
@AutoValue
public abstract class DailyForecast {
    /**
     * @return the date that this forecast is for
     */
    public abstract LocalDate getDate();

    /**
     * @return forecasted low temperature
     */
    public abstract int getLow();

    /**
     * @return forecasted high temperature
     */
    public abstract int getHigh();

    /**
     * @return description of forecasted conditions
     */
    public abstract String getText();

    @GsonBuilder
    public static Builder builder() {
        return new AutoValue_DailyForecast.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setDate(LocalDate date);
        public abstract Builder setLow(int low);
        public abstract Builder setHigh(int high);
        public abstract Builder setText(String text);
        public abstract DailyForecast build();
    }
}
