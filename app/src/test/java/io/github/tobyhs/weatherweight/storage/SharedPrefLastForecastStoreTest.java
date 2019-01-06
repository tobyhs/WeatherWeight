package io.github.tobyhs.weatherweight.storage;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.test.core.app.ApplicationProvider;

import java.io.IOException;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import io.github.tobyhs.weatherweight.AppModule;
import io.github.tobyhs.weatherweight.test.WeatherResponseFactory;
import io.github.tobyhs.weatherweight.yahooweather.model.Channel;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class SharedPrefLastForecastStoreTest {
    private SharedPreferences sharedPreferences;
    private SharedPrefLastForecastStore store;
    private Gson gson = AppModule.provideGson();

    @Before
    public void setup() {
        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(ApplicationProvider.getApplicationContext());
        store = new SharedPrefLastForecastStore(sharedPreferences, gson);
    }

    @Test
    public void getWithNoEntry() {
        assertThat(store.get().blockingGet(), is(nullValue()));
    }

    @Test
    public void getWithEntry() {
        Channel channel = WeatherResponseFactory.createChannel();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastForecast", gson.toJson(channel));
        editor.apply();

        Channel storedChannel = store.get().blockingGet();
        assertThat(storedChannel, is(channel));
    }

    @Test
    public void saveWithCommitSuccess() {
        Channel channel = WeatherResponseFactory.createChannel();

        assertThat(store.save(channel).blockingGet(), is(nullValue()));

        String json = sharedPreferences.getString("lastForecast", null);
        Channel storedChannel = gson.fromJson(json, Channel.class);
        assertThat(storedChannel, is(channel));
    }

    @Test
    public void saveWithCommitFailure() {
        Channel channel = WeatherResponseFactory.createChannel();
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);
        when(editor.commit()).thenReturn(false);
        store = new SharedPrefLastForecastStore(sharedPreferences, gson);

        Throwable error = store.save(channel).blockingGet();
        assertThat(error, is(instanceOf(IOException.class)));
    }
}
