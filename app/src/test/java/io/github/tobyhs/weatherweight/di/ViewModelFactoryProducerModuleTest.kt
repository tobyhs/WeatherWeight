package io.github.tobyhs.weatherweight.di

import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test

class ViewModelFactoryProducerModuleTest {
    @Test
    fun provideViewModelFactoryProducer() {
        assertThat(ViewModelFactoryProducerModule.provideViewModelFactoryProducer(), nullValue())
    }
}
