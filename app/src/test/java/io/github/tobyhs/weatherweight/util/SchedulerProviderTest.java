package io.github.tobyhs.weatherweight.util;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class SchedulerProviderTest {
    private SchedulerProvider provider = new SchedulerProvider();

    @Test
    public void testMain() {
        assertThat(provider.main(), is(AndroidSchedulers.mainThread()));
    }

    @Test
    public void testIo() {
        assertThat(provider.io(), is(Schedulers.io()));
    }
}
