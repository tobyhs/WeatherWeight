package io.github.tobyhs.weatherweight;

import android.content.SharedPreferences;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.github.tobyhs.weatherweight.storage.LastForecastStore;
import io.github.tobyhs.weatherweight.storage.SharedPrefLastForecastStore;
import io.github.tobyhs.weatherweight.test.BaseTestCase;
import io.github.tobyhs.weatherweight.util.AppSchedulerProvider;
import io.github.tobyhs.weatherweight.yahooweather.WeatherRepository;
import io.github.tobyhs.weatherweight.yahooweather.WeatherRepositoryImpl;
import io.github.tobyhs.weatherweight.yahooweather.WeatherService;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(application = App.class)
public class AppModuleTest extends BaseTestCase {
    private App app;
    private AppModule module;

    @Before
    public void setup() {
        app = (App) RuntimeEnvironment.application;
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
        assertThat(module.provideSchedulerProvider(), isA((Class) AppSchedulerProvider.class));
    }

    @Test
    public void provideYahooRetrofit() {
        Retrofit retrofit = module.provideYahooRetrofit();
        assertThat(retrofit.baseUrl().toString(), is("https://query.yahooapis.com/"));
        assertThat(retrofit.callAdapterFactories(), hasItem(isA(RxJava2CallAdapterFactory.class)));
        assertThat(retrofit.converterFactories(), hasItem(isA(LoganSquareConverterFactory.class)));
    }

    @Test
    public void provideWeatherService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost/").build();
        assertThat(module.provideWeatherService(retrofit), isA(WeatherService.class));
    }

    @Test
    public void provideWeatherRepository() {
        WeatherService weatherService = mock(WeatherService.class);
        WeatherRepository repo = module.provideWeatherRepository(weatherService);
        assertThat(repo, isA((Class) WeatherRepositoryImpl.class));
    }

    @Test
    public void provideLastForecastStore() {
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        LastForecastStore store = module.provideLastForecastStore(sharedPreferences);
        assertThat(store, isA((Class) SharedPrefLastForecastStore.class));
    }
}
