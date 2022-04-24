package io.github.tobyhs.weatherweight.storage

import android.content.SharedPreferences

import com.squareup.moshi.Moshi

import io.github.tobyhs.weatherweight.data.model.ForecastSearch

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

import java.io.IOException

/**
 * An implementation of [LastForecastStore] that uses Android's [android.content.SharedPreferences]
 */
class SharedPrefLastForecastStore
/**
 * @param sharedPreferences the [SharedPreferences] to use
 * @param moshi Moshi instance to serialize data
 */(private val sharedPreferences: SharedPreferences, private val moshi: Moshi) : LastForecastStore {
    override fun get(): Maybe<ForecastSearch> {
        return Maybe.fromCallable {
            val json = sharedPreferences.getString(KEY, null) ?: return@fromCallable null
            val moshiAdapter = moshi.adapter(ForecastSearch::class.java)
            moshiAdapter.fromJson(json)
        }
    }

    override fun save(forecastSearch: ForecastSearch): Completable {
        return Completable.fromAction {
            val moshiAdapter = moshi.adapter(ForecastSearch::class.java)
            val json = moshiAdapter.toJson(forecastSearch)
            val editor = sharedPreferences.edit()
            editor.putString(KEY, json)
            if (!editor.commit()) {
                throw IOException("Failed to write to SharedPreferences: $KEY")
            }
        }
    }

    companion object {
        private const val KEY = "lastForecast"
    }
}
