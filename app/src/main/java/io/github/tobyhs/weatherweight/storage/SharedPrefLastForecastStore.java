package io.github.tobyhs.weatherweight.storage;

import android.content.SharedPreferences;

import java.io.IOException;

import com.google.gson.Gson;

import io.reactivex.Completable;
import io.reactivex.Maybe;

import io.github.tobyhs.weatherweight.data.model.ForecastResultSet;

/**
 * An implementation of {@link LastForecastStore} that uses Android's
 * {@link android.content.SharedPreferences}
 */
public class SharedPrefLastForecastStore implements LastForecastStore {
    private static String KEY = "lastForecast";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    /**
     * @param sharedPreferences the {@link SharedPreferences} to use
     * @param gson Gson instance to serialize data
     */
    public SharedPrefLastForecastStore(SharedPreferences sharedPreferences, Gson gson) {
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
    }

    @Override
    public Maybe<ForecastResultSet> get() {
        return Maybe.fromCallable(() -> {
            String json = sharedPreferences.getString(KEY, null);
            if (json == null) {
                return null;
            }
            return gson.fromJson(json, ForecastResultSet.class);
        });
    }

    @Override
    public Completable save(final ForecastResultSet forecastResultSet) {
        return Completable.fromAction(() -> {
            String json = gson.toJson(forecastResultSet);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY, json);
            if (!editor.commit()) {
                throw new IOException("Failed to write to SharedPreferences: " + KEY);
            }
        });
    }
}
