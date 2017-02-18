package io.github.tobyhs.weatherweight;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import io.github.tobyhs.weatherweight.test.BaseTestCase;
import io.github.tobyhs.weatherweight.util.SchedulerProvider;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AppModuleTest extends BaseTestCase {
    @Mock private App app;
    @InjectMocks private AppModule module;

    @Test
    public void testProvideApp() {
        assertThat(module.provideApp(), is(app));
    }

    @Test
    public void testProvideSchedulerProvider() {
        Class schedulerProviderCls = module.provideSchedulerProvider().getClass();
        assertThat(schedulerProviderCls, is(equalTo((Class) SchedulerProvider.class)));
    }
}
