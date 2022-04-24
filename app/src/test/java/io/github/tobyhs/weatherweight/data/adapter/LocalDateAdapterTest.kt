package io.github.tobyhs.weatherweight.data.adapter

import java.time.LocalDate

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test

class LocalDateAdapterTest {
    private val adapter = LocalDateAdapter()

    @Test
    fun fromJson() {
        val date = adapter.fromJson("2022-12-30")
        assertThat(date.year, equalTo(2022))
        assertThat(date.monthValue, equalTo(12))
        assertThat(date.dayOfMonth, equalTo(30))
    }

    @Test
    fun toJson() {
        val date = LocalDate.of(2022, 11, 25)
        assertThat(adapter.toJson(date), equalTo("2022-11-25"))
    }
}
