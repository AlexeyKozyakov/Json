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
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.typeOf

inline fun <reified T> fromJson(input: String): T {
    val json = parseJson(input)
    return fromJson(json, typeOf<T>()) as T
}

fun fromJson(json: Json, type: KType, key: String? = null): Any? {
    return try {
        when {
            json == JsonNull -> if (type.isMarkedNullable) null else error("Nonnull value expected")

            type.classifier == String::class -> json.string()

            type.classifier == Int::class -> json.int()

            type.classifier == Double::class -> json.float()

            type.classifier == Boolean::class -> json.boolean()

            type.isSubtypeOf(typeOf<List<*>>()) -> {
                val values = json.array()
                val valueType = type.arguments.first().type
                check(valueType != null) { "Got unsupported List<*>" }
                values.map { innerJson ->
                    fromJson(innerJson, valueType, key)
                }
            }

            else -> {
                val kClass = checkNotNull(type.classifier as? KClass<*>) {
                    "Class not provided for object"
                }
                val constructor = checkNotNull(kClass.constructors.firstOrNull()) {
                    "At least one constructor expected for class"
                }
                val args = constructor.parameters.map { parameter ->
                    val innerKey = checkNotNull(parameter.name) {
                        "Constructor parameter name is required for class"
                    }
                    val value = json.obj().value[innerKey]?.let { innerJson ->
                        fromJson(innerJson, parameter.type, key = innerKey)
                    }
                    if (value == null) {
                        check(parameter.type.isMarkedNullable) {
                            "Required value of field $innerKey is not provided"
                        }
                    } else {
                        val parameterType = checkNotNull(parameter.type.classifier as? KClass<*>) {
                            "Type of field $innerKey is not supported"
                        }
                        check(value::class.isSubclassOf(parameterType)) {
                            "Expected type ${parameterType.simpleName} of field $innerKey"
                        }
                    }
                    value
                }
                constructor.call(*args.toTypedArray())
            }
        }
    } catch (e: ExceptionWrapper) {
        throw e
    } catch (e: Exception) {
        throw ExceptionWrapper(e.message + if (key != null) " for key: \"$key\"" else "", e)
    }
}

private class ExceptionWrapper(
    message: String,
    source: Exception
) : IllegalStateException(message, source)
