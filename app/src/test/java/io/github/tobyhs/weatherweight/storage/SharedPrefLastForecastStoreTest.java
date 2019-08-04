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
import io.github.tobyhs.weatherweight.data.model.ForecastSearch;
import io.github.tobyhs.weatherweight.test.ForecastSearchFactory;

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
        ForecastSearch forecastSearch = ForecastSearchFactory.create();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastForecast", gson.toJson(forecastSearch));
        editor.apply();

        ForecastSearch storedSearch = store.get().blockingGet();
        assertThat(storedSearch, is(forecastSearch));
    }

    @Test
    public void saveWithCommitSuccess() {
        ForecastSearch forecastSearch = ForecastSearchFactory.create();
        assertThat(store.save(forecastSearch).blockingGet(), is(nullValue()));

        String json = sharedPreferences.getString("lastForecast", null);
        ForecastSearch storedSearch = gson.fromJson(json, ForecastSearch.class);
        assertThat(storedSearch, is(forecastSearch));
    }

    @Test
    public void saveWithCommitFailure() {
        ForecastSearch forecastSearch = ForecastSearchFactory.create();
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);
        when(editor.commit()).thenReturn(false);
        store = new SharedPrefLastForecastStore(sharedPreferences, gson);

        Throwable error = store.save(forecastSearch).blockingGet();
        assertThat(error, is(instanceOf(IOException.class)));
    }
}
