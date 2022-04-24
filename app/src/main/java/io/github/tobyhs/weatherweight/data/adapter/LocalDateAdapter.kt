package io.github.tobyhs.weatherweight.data.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

import java.time.LocalDate

/**
 * Moshi adapter for java.time.LocalDate objects
 */
class LocalDateAdapter {
    /**
     * Converts a string in the format YYYY-MM-DD into a java.time.LocalDate
     *
     * @param string string in the format YYYY-MM-DD
     * @return the result LocalDate object
     */
    @FromJson fun fromJson(string: String): LocalDate = LocalDate.parse(string)

    /**
     * Serializes a java.time.LocalDate
     *
     * @param date the LocalDate to serialize
     * @return a JSON serialized string
     */
    @ToJson fun toJson(date: LocalDate): String = date.toString()
}