package io.github.alexeykozyakov.json.reflect

import io.github.alexeykozyakov.json.representation.Json

/**
 * Interface to implement custom mapping logic between [Json] and class of type [T].
 * Should be implemented on class companion object.
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
