package io.github.tobyhs.weatherweight.storage

import com.squareup.moshi.Moshi

import io.github.tobyhs.weatherweight.data.model.ForecastSearch

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

import java.io.File

/**
 * An implementation of [LastForecastCoroutinesStore] that uses a local file
 */
class FileLastForecastCoroutinesStore(
    private val directory: File,
    private val moshi: Moshi,
    private val ioDispatcher: CoroutineDispatcher,
) : LastForecastCoroutinesStore {
    private val file by lazy { File(directory, "lastForecast.json") }
    private val moshiAdapter by lazy { moshi.adapter(ForecastSearch::class.java) }

    override suspend fun get(): ForecastSearch? = withContext(ioDispatcher) {
        if (file.exists()) {
            return@withContext moshiAdapter.fromJson(file.readText())
        } else {
            return@withContext null
        }
    }

    override suspend fun save(forecastSearch: ForecastSearch) = withContext(ioDispatcher) {
        file.writeText(moshiAdapter.toJson(forecastSearch))
    }
}
