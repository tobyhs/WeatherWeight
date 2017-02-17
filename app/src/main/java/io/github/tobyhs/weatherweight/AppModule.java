package io.github.tobyhs.weatherweight;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import io.github.tobyhs.weatherweight.util.SchedulerProvider;

/**
 * Dagger module to provide common dependencies
 */
@Module
public class AppModule {
    private final App app;

    /**
     * @param app application instance
     */
    public AppModule(App app) {
        this.app = app;
    }

    /**
     * @return application instance
     */
    @Provides
    @Singleton
    App provideApp() {
        return app;
    }

    /**
     * @return RxJava scheduler provider
     */
    @Provides
    @Singleton
    SchedulerProvider provideSchedulerProvider() {
        return new SchedulerProvider();
    }
}
