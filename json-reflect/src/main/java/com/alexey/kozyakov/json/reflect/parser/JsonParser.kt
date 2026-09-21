package com.alexey.kozyakov.json.reflect.parser

import com.alexey.kozyakov.json.parser.parseJson
import com.alexey.kozyakov.json.representation.Json
import com.alexey.kozyakov.json.representation.JsonNull
import com.alexey.kozyakov.json.representation.array
import com.alexey.kozyakov.json.representation.boolean
import com.alexey.kozyakov.json.representation.float
import com.alexey.kozyakov.json.representation.int
import com.alexey.kozyakov.json.representation.obj
import com.alexey.kozyakov.json.representation.string
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSuperclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.typeOf

/**
 * Parses json from [input] string as type [T].
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

                Int::class -> json.int()

                Double::class -> json.float()

                Boolean::class -> json.boolean()

                Sequence::class -> listFromJson(json, type, key).asSequence()

                Byte::class,
                Short::class,
                Long::class,
                Float::class,
                Char::class -> error("Unsupported primitive type: ${kClass.simpleName}")

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
