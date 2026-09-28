package io.github.alexeykozyakov.json.reflect

/**
 * Allows to set custom JSON key name for class property or constructor parameter or
 * skip some properties during serialization.
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class JsonField(
    val name: String = "",
    val skip: Boolean = false
)
