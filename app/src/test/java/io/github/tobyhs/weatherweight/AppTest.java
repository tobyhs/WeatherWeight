package io.github.tobyhs.weatherweight;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(application = App.class)
public class AppTest {
    private App app;

    @Before
    public void setup() {
        app = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void applicationInjector() {
        assertThat(app.applicationInjector(), is(notNullValue()));
    }
}
