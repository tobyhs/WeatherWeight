package io.github.tobyhs.weatherweight.ui

/**
 * Represents the state of content that is loaded (Loading/Content/Error)
 *
 * @param T type of the content being loaded
 */
sealed class LoadState<T> {
    /**
     * When the content is loading
     */
    class Loading<T> : LoadState<T>() {
        override fun equals(other: Any?): Boolean = other is Loading<*>

        override fun hashCode(): Int = 1
    }

    /**
     * When the content was successfully loaded
     *
     * @property content the content that was successfully loaded
     */
    data class Content<T>(val content: T) : LoadState<T>()

    /**
     * When an error has occurred
     *
     * @property error the error that occurred
     */
    data class Error<T>(val error: Throwable) : LoadState<T>()
}
