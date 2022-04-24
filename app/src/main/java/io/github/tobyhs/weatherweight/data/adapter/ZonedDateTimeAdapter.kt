package io.github.tobyhs.weatherweight.data.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * Moshi adapter for java.time.ZonedDateTime objects
 */
class ZonedDateTimeAdapter {
    /**
     * Converts an ISO 8601 date-time into a java.time.ZonedDateTime
     *
     * @param string an ISO 8601 formatted date-time with time zone data
     * @return the resulting ZonedDateTime object
     */
    @FromJson fun fromJson(string: String): ZonedDateTime = ZonedDateTime.parse(string)

    /**
     * Serializes a java.util.ZonedDateTime
     *
     * @param time the ZonedDateTime object to serialize
     * @return a serialized string
     */
    @ToJson fun toJson(time: ZonedDateTime): String {
        return time.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }
}
