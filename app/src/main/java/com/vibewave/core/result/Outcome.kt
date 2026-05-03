package com.vibewave.core.result

/**
 * A discriminated union for async operations.
 * Using our own sealed class avoids collisions with kotlin.Result and
 * allows a Loading state for UI progress indicators.
 */
sealed interface Outcome<out T> {
    data object Loading : Outcome<Nothing>
    data class Success<T>(val data: T) : Outcome<T>
    data class Error(val message: String, val cause: Throwable? = null) : Outcome<Nothing>
}

inline fun <T, R> Outcome<T>.map(transform: (T) -> R): Outcome<R> = when (this) {
    is Outcome.Success -> Outcome.Success(transform(data))
    is Outcome.Error -> this
    Outcome.Loading -> Outcome.Loading
}
