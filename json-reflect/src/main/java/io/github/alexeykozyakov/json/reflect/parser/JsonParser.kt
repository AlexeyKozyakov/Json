package io.github.alexeykozyakov.json.reflect.parser

import io.github.alexeykozyakov.json.accessors.array
import io.github.alexeykozyakov.json.accessors.boolean
import io.github.alexeykozyakov.json.accessors.byte
import io.github.alexeykozyakov.json.accessors.double
import io.github.alexeykozyakov.json.accessors.float
import io.github.alexeykozyakov.json.accessors.int
import io.github.alexeykozyakov.json.accessors.long
import io.github.alexeykozyakov.json.accessors.obj
import io.github.alexeykozyakov.json.accessors.short
import io.github.alexeykozyakov.json.accessors.string
import io.github.alexeykozyakov.json.parser.parseJson
import io.github.alexeykozyakov.json.representation.Json
import io.github.alexeykozyakov.json.representation.JsonNull
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSuperclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.typeOf

/**
 * Parses JSON from [input] string as type [T].
 *
 *
 * T can be one of the following:
 *
 * T <- String, Number, Boolean
 *
 * T <- T?
 *
 * T <- List<T>, Sequence<T>, Iterable<T>, Collection<T>
 *
 * T <- class(val t1: T1, val t2: T2, ...)
 *
 * @throws IllegalStateException if parsing error is occurred
 */
inline fun <reified T> fromJson(input: String): T {
    val json = parseJson(input)
    return fromJson(json, typeOf<T>()) as T
}

fun fromJson(json: Json, type: KType, key: String? = null): Any? {
    return try {
        if (json == JsonNull) {
            if (type.isMarkedNullable) null else error("Nonnull value expected")
        } else {
            val kClass = checkNotNull(type.classifier as? KClass<*>) { "Class not provided" }
            when (kClass) {
                String::class -> json.string()

                Boolean::class -> json.boolean()

                Long::class -> json.long()

                Short::class -> json.short()

                Byte::class -> json.byte()

                Int::class -> json.int()

                Double::class -> json.double()

                Float::class -> json.float()

                Char::class -> error("Unsupported primitive type Char")

                Sequence::class -> listFromJson(json, type, key).asSequence()

                else -> {
                    if (kClass.isSuperclassOf(List::class)) {
                        listFromJson(json, type, key)
                    } else {
                        objectFromJson(json, kClass)
                    }
                }
            }
        }
    } catch (e: ExceptionWrapper) {
        throw e
    } catch (e: Exception) {
        throw ExceptionWrapper(e.message + if (key != null) " for key: \"$key\"" else "", e)
    }
}

private fun listFromJson(json: Json, type: KType, key: String?): List<Any?> {
    val values = json.array()
    val valueType = type.arguments.first().type
    check(valueType != null) { "Got unsupported List<*>" }
    return values.map { innerJson ->
        fromJson(innerJson, valueType, key)
    }
}

private fun objectFromJson(json: Json, kClass: KClass<*>): Any {
    val constructor = checkNotNull(kClass.primaryConstructor) {
        "Primary constructor of class ${kClass.simpleName} not found"
    }
    val args = constructor.parameters.map { parameter ->
        val innerKey = checkNotNull(parameter.name) {
            "Constructor parameter name of class ${kClass.simpleName} is required"
        }
        val value = json.obj().value[innerKey]?.let { innerJson ->
            fromJson(innerJson, parameter.type, key = innerKey)
        }
        if (value == null) {
            check(parameter.type.isMarkedNullable) {
                "Required value for inner key \"$innerKey\" is not provided"
            }
        }
        value
    }
    return constructor.call(*args.toTypedArray())
}

private class ExceptionWrapper(
    message: String,
    source: Exception
) : IllegalStateException(message, source)
