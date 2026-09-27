package io.github.alexeykozyakov.json.reflect

/**
 * Allows to set custom JSON key name for class property or constructor parameter.
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class JsonName(val name: String)
