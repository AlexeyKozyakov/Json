package io.github.alexeykozyakov.json.reflect

import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

private const val INSTANCE_FIELD = "INSTANCE"
private const val HIDDEN_INSTANCE_FIELD = "$$$INSTANCE_FIELD"

internal val isAndroid by lazy {
    try {
        Class.forName("android.os.Build")
        true
    } catch (_: ClassNotFoundException) {
        false
    }
}

internal fun <T> KCallable<T>.allowAccessAndCall(vararg args: Any?): T {
    isAccessible = true
    return call(*args)
}

internal fun <T> KCallable<T>.allowAccessAndCallBy(args: Map<KParameter, Any?>): T {
    isAccessible = true
    return callBy(args)
}

internal fun KClass<*>.getCompanionObject(): Any? {
    val companionClass = companionObject ?: return null
    val companionJavaClass = companionClass.java
    val instanceField = companionJavaClass
        .declaredFields
        .firstOrNull { field ->
            field.name == INSTANCE_FIELD || field.name == HIDDEN_INSTANCE_FIELD
        } ?: companionJavaClass.enclosingClass
        ?.declaredFields?.firstOrNull { field ->
            field.name == companionClass.simpleName
        } ?: return null
    instanceField.isAccessible = true
    return instanceField.get(null)
}

internal fun KClass<*>.getPropertiesInDeclarationOrderIfPossible(): Collection<KProperty1<*, *>> {
    return if (isData) {
        val constructor = primaryConstructor!!
        val propertiesMap = declaredMemberProperties.associateBy { property ->
            property.name
        }
        constructor.parameters.map { parameter ->
            propertiesMap[parameter.name]!!
        }
    } else {
        declaredMemberProperties
    }
}
