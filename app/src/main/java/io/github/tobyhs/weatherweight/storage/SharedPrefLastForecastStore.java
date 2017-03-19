package io.github.tobyhs.weatherweight.storage;

import android.content.SharedPreferences;

import java.io.IOException;
import java.util.concurrent.Callable;

import com.bluelinelabs.logansquare.LoganSquare;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.functions.Action;

import io.github.tobyhs.weatherweight.yahooweather.model.Channel;

/**
 * An implementation of {@link LastForecastStore} that uses Android's
 * {@link android.content.SharedPreferences}
 */
public class SharedPrefLastForecastStore implements LastForecastStore {
    private static String KEY = "lastForecast";

    private SharedPreferences sharedPreferences;

    /**
     * @param sharedPreferences the {@link SharedPreferences} to use
     */
    public SharedPrefLastForecastStore(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public Maybe<Channel> get() {
        return Maybe.fromCallable(new Callable<Channel>() {
            @Override
            public Channel call() throws Exception {
                String json = sharedPreferences.getString(KEY, null);
                if (json == null) {
                    return null;
                }
                return LoganSquare.parse(json, Channel.class);
            }
        });
    }

    @Override
    public Completable save(final Channel channel) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                String json = LoganSquare.serialize(channel);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY, json);
                if (!editor.commit()) {
                    throw new IOException("Failed to write to SharedPreferences: " + KEY);
                }
            }
        });
    }
}
