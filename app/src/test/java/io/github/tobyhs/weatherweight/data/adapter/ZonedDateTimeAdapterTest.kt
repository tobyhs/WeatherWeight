package io.github.tobyhs.weatherweight.data.adapter

import java.time.ZoneOffset
import java.time.ZonedDateTime

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test

class ZonedDateTimeAdapterTest {
    private val adapter = ZonedDateTimeAdapter()

    @Test
    fun fromJson() {
        val time = adapter.fromJson("2019-01-13T16:55:40-08:00")
        assertThat(time.year, equalTo(2019))
        assertThat(time.monthValue, equalTo(1))
        assertThat(time.dayOfMonth, equalTo(13))
        assertThat(time.hour, equalTo(16))
        assertThat(time.minute, equalTo(55))
        assertThat(time.second, equalTo(40))
        assertThat(time.offset, equalTo(ZoneOffset.ofHours(-8)))
    }

    @Test
    fun toJson() {
        val time = ZonedDateTime.of(2022, 3, 30, 23, 40, 50, 0, ZoneOffset.ofHours(-8))
        assertThat(adapter.toJson(time), equalTo("2022-03-30T23:40:50-08:00"))
    }
}
