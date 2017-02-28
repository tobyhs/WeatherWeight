package io.github.tobyhs.weatherweight.forecast;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import io.github.tobyhs.weatherweight.test.BaseTestCase;
import io.github.tobyhs.weatherweight.test.TestSchedulerProvider;
import io.github.tobyhs.weatherweight.yahooweather.WeatherRepository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ForecastPresenterTest extends BaseTestCase {
    private TestSchedulerProvider schedulerProvider = new TestSchedulerProvider();
    @Mock private WeatherRepository weatherRepository;
    private ForecastPresenter presenter;

    @Before
    public void setup() {
        presenter = new ForecastPresenter(schedulerProvider, weatherRepository);
    }

    @Test
    public void getAttributionUrl() {
        assertThat(presenter.getAttributionUrl(), is(WeatherRepository.ATTRIBUTION_URL));
    }
}
