package io.github.tobyhs.weatherweight.util;

import io.reactivex.android.schedulers.AndroidSchedulers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class AppSchedulerProviderTest {
    private AppSchedulerProvider provider = new AppSchedulerProvider();

    @Test
    public void ui() {
        assertThat(provider.ui(), is(AndroidSchedulers.mainThread()));
    }
}
