package io.github.tobyhs.weatherweight.util;

import io.reactivex.Scheduler;

/**
 * Interface for an object that provides RxJava schedulers.
 */
public interface SchedulerProvider {
    /**
     * @return scheduler for UI thread
     */
    Scheduler ui();

    /**
     * @return scheduler for IO-bound work
     */
    Scheduler io();
}
