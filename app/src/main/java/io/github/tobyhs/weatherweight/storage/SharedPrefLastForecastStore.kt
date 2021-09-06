package io.github.tobyhs.weatherweight.storage

import android.content.SharedPreferences

import com.google.gson.Gson

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
 * @param gson Gson instance to serialize data
 */(private val sharedPreferences: SharedPreferences, private val gson: Gson) : LastForecastStore {
    override fun get(): Maybe<ForecastSearch> {
        return Maybe.fromCallable {
            val json = sharedPreferences.getString(KEY, null) ?: return@fromCallable null
            gson.fromJson(json, ForecastSearch::class.java)
        }
    }

    override fun save(forecastSearch: ForecastSearch): Completable {
        return Completable.fromAction {
            val json = gson.toJson(forecastSearch)
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
