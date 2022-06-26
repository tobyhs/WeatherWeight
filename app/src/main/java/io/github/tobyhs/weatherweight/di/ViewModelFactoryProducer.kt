package io.github.tobyhs.weatherweight.di

import androidx.lifecycle.ViewModelProvider

typealias ViewModelFactoryProducer = () -> @JvmSuppressWildcards ViewModelProvider.Factory
