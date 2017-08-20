package io.github.tobyhs.weatherweight.util;

import com.github.tobyhs.rxsecretary.SchedulerProvider;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * A {@link SchedulerProvider} for an Android app
 */
public class AppSchedulerProvider implements SchedulerProvider {
    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler io() {
        return Schedulers.io();
    }
}
