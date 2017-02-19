package io.github.tobyhs.weatherweight;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class AppTest {
    private App app;

    @Before
    public void setup() {
        app = (App) RuntimeEnvironment.application;
    }

    @Test
    public void testGetAppComponent() {
        assertThat(app.getAppComponent(), is(notNullValue()));
    }
}
