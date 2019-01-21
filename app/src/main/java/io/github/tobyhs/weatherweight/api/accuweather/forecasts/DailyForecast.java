package io.github.tobyhs.weatherweight.api.accuweather.forecasts;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import me.tatarka.gsonvalue.annotations.GsonBuilder;
import org.threeten.bp.ZonedDateTime;

/**
 * A forecast for a day from AccuWeather's Forecasts API
 */
@AutoValue
public abstract class DailyForecast {
    /**
     * @return the date that this forecast is for
     */
    @SerializedName("Date")
    public abstract ZonedDateTime getDate();

    /**
     * @return the forecasted minimum and maximum temperatures
     */
    @SerializedName("Temperature")
    public abstract TemperatureRange getTemperature();

    /**
     * @return forecast details about the day
     */
    @SerializedName("Day")
    public abstract DayPeriod getDay();

    /**
     * @return forecast details about the night
     */
    @SerializedName("Night")
    public abstract DayPeriod getNight();

    @GsonBuilder
    public static Builder builder() {
        return new AutoValue_DailyForecast.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder setDate(ZonedDateTime date);
        public abstract Builder setTemperature(TemperatureRange temperature);
        public abstract Builder setDay(DayPeriod day);
        public abstract Builder setNight(DayPeriod night);
        public abstract DailyForecast build();
    }
}
