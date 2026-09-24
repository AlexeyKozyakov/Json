package io.github.alexeykozyakov.json.reflect

import io.github.alexeykozyakov.json.representation.Json

/**
 * Interface to implement custom [Json] <-> class mapping logic.
 * Should be implemented on class companion object.
 * Implemented mapping methods will be used
 * by [fromJson] and [toJson] functions.
 */
interface JsonMapper<T: Any> {
    /**
     * Maps value of type [T] to [Json] representation.
     */
    fun toJson(value: T): Json

    /**
     * Maps [Json] representation to value of type [T]
     */
    fun fromJson(json: Json): T
}
