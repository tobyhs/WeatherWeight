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
import io.github.tobyhs.weatherweight.data.model.ForecastResultSet;
import io.github.tobyhs.weatherweight.test.ForecastResultSetFactory;

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
        ForecastResultSet forecastResultSet = ForecastResultSetFactory.create();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastForecast", gson.toJson(forecastResultSet));
        editor.apply();

        ForecastResultSet storedForecast = store.get().blockingGet();
        assertThat(storedForecast, is(forecastResultSet));
    }

    @Test
    public void saveWithCommitSuccess() {
        ForecastResultSet forecastResultSet = ForecastResultSetFactory.create();

        assertThat(store.save(forecastResultSet).blockingGet(), is(nullValue()));

        String json = sharedPreferences.getString("lastForecast", null);
        ForecastResultSet storedForecast = gson.fromJson(json, ForecastResultSet.class);
        assertThat(storedForecast, is(forecastResultSet));
    }

    @Test
    public void saveWithCommitFailure() {
        ForecastResultSet forecastResultSet = ForecastResultSetFactory.create();
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);
        when(editor.commit()).thenReturn(false);
        store = new SharedPrefLastForecastStore(sharedPreferences, gson);

        Throwable error = store.save(forecastResultSet).blockingGet();
        assertThat(error, is(instanceOf(IOException.class)));
    }
}
