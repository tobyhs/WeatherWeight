package io.github.tobyhs.weatherweight.api.accuweather;

import okhttp3.Interceptor;
import okhttp3.Request;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccuWeatherApiKeyInterceptorTest {
    @Test
    public void intercept() throws Exception {
        String apiKey = "def987abc";
        AccuWeatherApiKeyInterceptor interceptor = new AccuWeatherApiKeyInterceptor(apiKey);
        Interceptor.Chain chain = mock(Interceptor.Chain.class);
        String url = "https://dataservice.accuweather.com/locations/v1/cities/search?q=Springfield";
        Request originalRequest = new Request.Builder().get().url(url).build();
        when(chain.request()).thenReturn(originalRequest);

        ArgumentCaptor<Request> argument = ArgumentCaptor.forClass(Request.class);
        interceptor.intercept(chain);
        verify(chain).proceed(argument.capture());
        assertThat(argument.getValue().method(), is("GET"));
        assertThat(argument.getValue().url().toString(), is(url + "&apikey=" + apiKey));

    }
}
