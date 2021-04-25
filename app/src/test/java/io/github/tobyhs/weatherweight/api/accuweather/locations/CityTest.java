package io.github.tobyhs.weatherweight.api.accuweather.locations;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Test;

import io.github.tobyhs.weatherweight.AppModule;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CityTest {
    @Test
    public void parseJson() throws Exception {
        try (
                InputStream stream = getClass().getResourceAsStream("/accuweather/city.json");
                Reader reader = new InputStreamReader(stream)
        ) {
            Gson gson = AppModule.provideGson();
            City city = gson.fromJson(reader, City.class);

            assertThat(city.getKey(), is("347629"));
            assertThat(city.getLocalizedName(), is("San Francisco"));
            assertThat(city.getAdministrativeArea().getId(), is("CA"));
            assertThat(city.getCountry().getId(), is("US"));
        }
    }
}
