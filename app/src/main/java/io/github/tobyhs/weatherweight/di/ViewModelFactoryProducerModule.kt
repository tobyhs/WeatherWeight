package io.github.tobyhs.weatherweight.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

/**
 * Dagger module to provide a [ViewModelFactoryProducer]
 */
@Module
@InstallIn(SingletonComponent::class)
object ViewModelFactoryProducerModule {
    /**
     * @return function that provides a view model factory, or null to use the default
     */
    @Provides
    @Singleton
    fun provideViewModelFactoryProducer(): ViewModelFactoryProducer? = null
}
