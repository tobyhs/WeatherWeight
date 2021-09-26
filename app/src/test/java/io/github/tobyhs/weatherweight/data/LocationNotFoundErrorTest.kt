package io.github.tobyhs.weatherweight.data

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test

class LocationNotFoundErrorTest {
    @Test
    fun constructorSetsMessage() {
        val error = LocationNotFoundError("Nowhere")
        assertThat(error.message, equalTo("Location Nowhere not found"))
    }
}
