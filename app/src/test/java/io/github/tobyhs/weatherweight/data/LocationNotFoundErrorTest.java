package io.github.tobyhs.weatherweight.data;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LocationNotFoundErrorTest {
    @Test
    public void constructorSetsMessage() {
        LocationNotFoundError error = new LocationNotFoundError("Nowhere");
        assertThat(error.getMessage(), is("Location Nowhere not found"));
    }
}
