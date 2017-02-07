package io.github.tobyhs.weatherweight.yahooweather.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LocationTest {
    @Test
    public void toStringReturnsCommaDelimitedComponents() {
        Location location = new Location().setCity("Oakland").setRegion("CA").setCountry("USA");
        assertThat(location.toString(), is("Oakland, CA, USA"));
    }
}
