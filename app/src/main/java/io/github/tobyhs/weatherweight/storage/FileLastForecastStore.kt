package io.github.tobyhs.weatherweight.storage

import com.squareup.moshi.Moshi

import io.github.tobyhs.weatherweight.data.model.ForecastSearch

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

import java.io.File

/**
 * An implementation of [LastForecastStore] that uses a local file
 */
class FileLastForecastStore
/**
 * @param directory the directory where the file will reside
 * @param moshi Moshi instance to serialize data
 */(internal val directory: File, internal val moshi: Moshi) : LastForecastStore {
    private val file by lazy { File(directory, "lastForecast.json") }
    private val moshiAdapter by lazy { moshi.adapter(ForecastSearch::class.java) }

    override fun get(): Maybe<ForecastSearch> {
        return Maybe.create {
            if (file.exists()) {
                it.onSuccess(moshiAdapter.fromJson(file.readText())!!)
            } else {
                it.onComplete()
            }
        }
    }

    override fun save(forecastSearch: ForecastSearch): Completable {
        return Completable.fromAction {
            file.writeText(moshiAdapter.toJson(forecastSearch))
        }
    }
}
