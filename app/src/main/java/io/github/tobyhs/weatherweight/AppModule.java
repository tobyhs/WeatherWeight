package io.github.tobyhs.weatherweight;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Named;
import javax.inject.Singleton;

import com.github.tobyhs.rxsecretary.SchedulerProvider;
import com.github.tobyhs.rxsecretary.android.AndroidSchedulerProvider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;

import okhttp3.OkHttpClient;

import org.aaronhe.threetengson.ThreeTenGsonAdapter;

import org.threeten.bp.Clock;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import io.github.tobyhs.weatherweight.api.accuweather.AccuWeatherApiKeyInterceptor;
import io.github.tobyhs.weatherweight.api.accuweather.AccuWeatherService;
import io.github.tobyhs.weatherweight.data.AccuWeatherRepository;
import io.github.tobyhs.weatherweight.data.WeatherRepository;
import io.github.tobyhs.weatherweight.storage.LastForecastStore;
import io.github.tobyhs.weatherweight.storage.SharedPrefLastForecastStore;
import io.github.tobyhs.weatherweight.util.GVTypeAdapterFactory;

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
     * @return {@link SharedPreferences} instance for the application
     */
    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(app);
    }

    /**
     * @return RxJava scheduler provider
     */
    @Provides
    @Singleton
    SchedulerProvider provideSchedulerProvider() {
        return new AndroidSchedulerProvider();
    }

    /**
     * @return a Gson instance
     */
    @Provides
    @Singleton
    public static Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapterFactory(GVTypeAdapterFactory.create());
        return ThreeTenGsonAdapter.registerAll(gsonBuilder).create();
    }

    /**
     * @param gson Gson instance for parsing JSON response bodies
     * @return a Retrofit instance for AccuWeather's APIs
     */
    @Provides @Named("accuWeatherRetrofit")
    @Singleton
    Retrofit provideAccuWeatherRetrofit(Gson gson) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new AccuWeatherApiKeyInterceptor(BuildConfig.ACCUWEATHER_API_KEY))
                .build();
        return new Retrofit.Builder()
                .baseUrl("https://dataservice.accuweather.com/")
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    /**
     * @param retrofit a Retrofit instance for AccuWeather's APIs
     * @return Retrofit service object for querying weather info from AccuWeather's APIs
     */
    @Provides
    @Singleton
    AccuWeatherService provideAccuWeatherService(@Named("accuWeatherRetrofit") Retrofit retrofit) {
        return retrofit.create(AccuWeatherService.class);
    }

    /**
     * @param service Retrofit service object for querying weather data from AccuWeather's APIs
     * @return repository object to fetch weather data
     */
    @Provides
    @Singleton
    WeatherRepository provideWeatherRepository(AccuWeatherService service) {
        return new AccuWeatherRepository(service, Clock.systemDefaultZone());
    }

    /**
     * @param sharedPreferences {@link SharedPreferences} instance for the application
     * @param gson Gson instance to serialize data
     * @return store to save or get the last forecast
     */
    @Provides
    @Singleton
    LastForecastStore provideLastForecastStore(SharedPreferences sharedPreferences, Gson gson) {
        return new SharedPrefLastForecastStore(sharedPreferences, gson);
    }
}
