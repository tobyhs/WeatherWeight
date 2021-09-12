package io.github.tobyhs.weatherweight

import androidx.test.core.app.ApplicationProvider

import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = App::class)
class AppTest {
    private lateinit var app: App

    @Before
    fun setup() {
        app = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun applicationInjector() {
        assertThat(app.applicationInjector(), instanceOf(DaggerAppComponent::class.java))
    }
}
