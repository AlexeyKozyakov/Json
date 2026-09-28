package io.github.alexeykozyakov.json.reflect

/**
 * Sets custom JSON field name for class property or constructor parameter.
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class JsonName(val name: String)

/**
 * Property marked with this annotation will be skipped during serialization.
 */
@Target(AnnotationTarget.PROPERTY)
annotation class JsonSkip
