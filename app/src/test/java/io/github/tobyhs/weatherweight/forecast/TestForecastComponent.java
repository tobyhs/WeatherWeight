package io.github.tobyhs.weatherweight.forecast;

import static org.mockito.Mockito.mock;

/**
 * {@link ForecastComponent} for unit tests
 */
public class TestForecastComponent implements ForecastComponent {
    @Override
    public ForecastPresenter forecastPresenter() {
        return mock(ForecastPresenter.class);
    }
}
