package io.github.alexeykozyakov.json.reflect

import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.jvm.isAccessible

private const val INSTANCE_FIELD = "INSTANCE"
private const val HIDDEN_INSTANCE_FIELD = "$$$INSTANCE_FIELD"

internal fun <T> KCallable<T>.allowAccessAndCall(vararg args: Any?): T {
    isAccessible = true
    return call(*args)
}

internal fun KClassifier.getCompanionObject(): Any? {
    val kClass = this as? KClass<*> ?: return null
    try {
        return kClass.companionObjectInstance
    } catch (_: IllegalAccessException) {
        // Fallback to tricky java reflection if companion object is inaccessible
        val companionClass = kClass.companionObject ?: return null
        val companionJavaClass = companionClass.java
        val instanceField = companionJavaClass
            .declaredFields
            .firstOrNull { field ->
                field.name == INSTANCE_FIELD || field.name == HIDDEN_INSTANCE_FIELD
            } ?: companionJavaClass.enclosingClass
            ?.declaredFields?.firstOrNull { field ->
                field.name == companionClass.simpleName
            } ?: return null
        if (!instanceField.trySetAccessible()) return null
        return instanceField.get(null)
    }
}
