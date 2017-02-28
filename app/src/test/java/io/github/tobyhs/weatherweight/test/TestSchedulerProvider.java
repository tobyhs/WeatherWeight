package io.github.tobyhs.weatherweight.test;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.TestScheduler;

import io.github.tobyhs.weatherweight.util.SchedulerProvider;

/**
 * A {@link SchedulerProvider} that provides a {@link TestScheduler} instance for testing.
 */
public class TestSchedulerProvider implements SchedulerProvider {
    private final Scheduler scheduler = new TestScheduler();

    @Override
    public Scheduler ui() {
        return scheduler;
    }

    @Override
    public Scheduler io() {
        return scheduler;
    }
}
