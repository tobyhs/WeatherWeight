package io.github.tobyhs.weatherweight.util;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * This provides RxJava schedulers.
 */
public class SchedulerProvider {
    /**
     * @return scheduler for UI thread
     */
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }

    /**
     * @return scheduler for IO-bound work
     */
    public Scheduler io() {
        return Schedulers.io();
    }
}
