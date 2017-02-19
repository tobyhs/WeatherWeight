package io.github.tobyhs.weatherweight;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import retrofit2.Retrofit;

import io.github.tobyhs.weatherweight.test.BaseTestCase;
import io.github.tobyhs.weatherweight.util.SchedulerProvider;
import io.github.tobyhs.weatherweight.yahooweather.WeatherRepository;
import io.github.tobyhs.weatherweight.yahooweather.WeatherRepositoryImpl;
import io.github.tobyhs.weatherweight.yahooweather.WeatherService;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class AppModuleTest extends BaseTestCase {
    @Mock private App app;
    @InjectMocks private AppModule module;

    @Test
    public void testProvideApp() {
        assertThat(module.provideApp(), is(app));
    }

    @Test
    public void testProvideSchedulerProvider() {
        Class schedulerProviderCls = module.provideSchedulerProvider().getClass();
        assertThat(schedulerProviderCls, is(equalTo((Class) SchedulerProvider.class)));
    }

    @Test
    public void testProvideYahooRetrofit() {
        Retrofit retrofit = module.provideYahooRetrofit();
        assertThat(retrofit.baseUrl().toString(), is("https://query.yahooapis.com/"));
        assertThat(retrofit.callAdapterFactories(), hasItem(isA(RxJava2CallAdapterFactory.class)));
        assertThat(retrofit.converterFactories(), hasItem(isA(LoganSquareConverterFactory.class)));
    }

    @Test
    public void testProvideWeatherService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost/").build();
        assertThat(module.provideWeatherService(retrofit), isA(WeatherService.class));
    }

    @Test
    public void testProvideWeatherRepository() {
        WeatherService weatherService = mock(WeatherService.class);
        WeatherRepository repo = module.provideWeatherRepository(weatherService);
        assertThat(repo.getClass(), is(equalTo((Class) WeatherRepositoryImpl.class)));
    }
}
