package io.github.tobyhs.weatherweight;

import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;

import com.github.tobyhs.rxsecretary.android.AndroidSchedulerProvider;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import io.github.tobyhs.weatherweight.api.accuweather.AccuWeatherApiKeyInterceptor;
import io.github.tobyhs.weatherweight.api.accuweather.AccuWeatherService;
import io.github.tobyhs.weatherweight.data.AccuWeatherRepository;
import io.github.tobyhs.weatherweight.data.WeatherRepository;
import io.github.tobyhs.weatherweight.storage.LastForecastStore;
import io.github.tobyhs.weatherweight.storage.SharedPrefLastForecastStore;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(application = App.class)
public class AppModuleTest {
    private App app;
    private AppModule module;

    @Before
    public void setup() {
        app = ApplicationProvider.getApplicationContext();
        module = new AppModule(app);
    }

    @Test
    public void provideApp() {
        assertThat(module.provideApp(), is(app));
    }

    @Test
    public void provideSharedPreferences() {
        assertThat(module.provideSharedPreferences(), is(notNullValue()));
    }

    @Test
    public void provideSchedulerProvider() {
        assertThat(module.provideSchedulerProvider() instanceof AndroidSchedulerProvider, is(true));
    }

    @Test
    public void provideGson() {
        assertThat(AppModule.provideGson(), is(notNullValue()));
    }

    @Test
    public void provideAccuWeatherOkHttp() {
        OkHttpClient client = module.provideAccuWeatherOkHttp();
        assertThat(client.interceptors(), hasItem(isA(AccuWeatherApiKeyInterceptor.class)));
        assertThat(client.cache(), is(notNullValue()));
    }

    @Test
    public void provideAccuWeatherRetrofit() {
        OkHttpClient client = mock(OkHttpClient.class);
        Retrofit retrofit = module.provideAccuWeatherRetrofit(AppModule.provideGson(), client);
        assertThat(retrofit.baseUrl().toString(), is("https://dataservice.accuweather.com/"));
        assertThat(retrofit.callFactory(), is(client));
        assertThat(retrofit.callAdapterFactories(), hasItem(isA(RxJava2CallAdapterFactory.class)));
        assertThat(retrofit.converterFactories(), hasItem(isA(GsonConverterFactory.class)));
    }

    @Test
    public void provideAccuWeatherService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost/").build();
        assertThat(module.provideAccuWeatherService(retrofit), is(notNullValue()));
    }

    @Test
    public void provideWeatherRepository() {
        AccuWeatherService service = mock(AccuWeatherService.class);
        WeatherRepository repo = module.provideWeatherRepository(service);
        assertThat(repo instanceof AccuWeatherRepository, is(true));
    }

    @Test
    public void provideLastForecastStore() {
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        Gson gson = AppModule.provideGson();
        LastForecastStore store = module.provideLastForecastStore(sharedPreferences, gson);
        assertThat(store instanceof SharedPrefLastForecastStore, is(true));
    }
}
