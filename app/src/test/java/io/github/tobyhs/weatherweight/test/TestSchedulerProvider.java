package io.github.tobyhs.weatherweight.test;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.TestScheduler;

import io.github.tobyhs.weatherweight.util.SchedulerProvider;

/**
 * A {@link SchedulerProvider} that provides {@link TestScheduler} instances for testing.
 */
public class TestSchedulerProvider implements SchedulerProvider {
    private final TestScheduler ioScheduler = new TestScheduler();
    private final TestScheduler uiScheduler = new TestScheduler();

    @Override
    public Scheduler io() {
        return ioScheduler;
    }

    @Override
    public Scheduler ui() {
        return uiScheduler;
    }

    /**
     * Triggers actions on all of this provider's schedulers
     */
    public void triggerActions() {
        ioScheduler.triggerActions();
        uiScheduler.triggerActions();
    }
}
