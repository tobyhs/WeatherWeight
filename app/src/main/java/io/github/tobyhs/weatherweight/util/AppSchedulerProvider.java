package io.github.tobyhs.weatherweight.util;

import com.github.tobyhs.rxsecretary.BaseSchedulerProvider;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * A {@link com.github.tobyhs.rxsecretary.SchedulerProvider} for an Android app
 */
public class AppSchedulerProvider extends BaseSchedulerProvider {
    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}
