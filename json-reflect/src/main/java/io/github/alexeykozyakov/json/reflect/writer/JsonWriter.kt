package io.github.alexeykozyakov.json.reflect.writer

import io.github.alexeykozyakov.json.reflect.JsonMapper
import io.github.alexeykozyakov.json.reflect.allowAccessAndCall
import io.github.alexeykozyakov.json.reflect.getCompanionObject
import io.github.alexeykozyakov.json.reflect.getPropertiesInDeclarationOrderIfPossible
import io.github.alexeykozyakov.json.representation.*
import io.github.alexeykozyakov.json.writer.writeJson
import kotlin.reflect.KType
import kotlin.reflect.typeOf

/**
 * Writes value of subtype of [T] to JSON string.
 *
 * T can be one of the following:
 *
 *  - String
 *  - Long, Int, Short, Byte, Double, Float
 *  - Number
 *  - Boolean
 *  - Enum
 *  - Any
 *  - T?
 *  - class with custom JsonMapper
 *  - List<T>, Sequence<T>, Iterable<T>, Collection<T>, Map<String, T>
 *  - class with properties of type T1, T2, ... Tn
 *
 * @param omitNulls enables omitting of null properties during class serialization.
 *
 * @throws JsonWritingException if provided type cannot be written to JSON.
 */
inline fun <reified T> T.toJson(omitNulls: Boolean = true): String {
    val json = toJson(this, typeOf<T>(), omitNulls)
    return writeJson(json)
}

/**
 * Maps value of subtype of [T] to [Json].
 *
 * T can be one of the following:
 *
 *  - String
 *  - Long, Int, Short, Byte, Double, Float
 *  - Number
 *  - Boolean
 *  - Enum
 *  - Any
 *  - T?
 *  - class with custom JsonMapper
 *  - List<T>, Sequence<T>, Iterable<T>, Collection<T>, Map<String, T>
 *  - class with properties of type T1, T2, ... Tn
 *
 * @param omitNulls enables omitting of null properties during mapping.
 *
 * @throws JsonWritingException if provided type cannot be mapped to [Json].
 */
inline fun <reified T> T.toJsonRepresentation(omitNulls: Boolean = true): Json {
    return toJson(this, typeOf<T>(), omitNulls)
}

/**
 * Exception which can be thrown while JSON writing if
 * it contains unsupported fields.
 */
class JsonWritingException(message: String, cause: Exception? = null) :
    IllegalArgumentException(message, cause)

/**
 * Internal function, use [toJson] or [toJsonRepresentation] instead.
 */
fun toJson(value: Any?, type: KType?, omitNulls: Boolean): Json {
    return when (value) {
        is String -> JsonString(value)

        is Number -> JsonNumber(value)

        is Boolean -> JsonBoolean(value)

        is Enum<*> -> JsonString(value.name)

        null -> JsonNull

        is Char -> writingError("Unsupported primitive type Char")

        is Map<*, *> -> {
            val innerType = type?.arguments?.getOrNull(1)?.type
            val innerValues = mutableMapOf<String, Json>()
            value.forEach { (key, innerValue) ->
                if (key == null) writingError("Keys of map should be nonnull")
                if (key !is String) writingError("Only String type of map keys is supported")
                innerValues[key] = toJson(innerValue, innerType, omitNulls)
            }
            JsonObject(value = innerValues)
        }

        is Iterable<*>, is Sequence<*> -> {
            val innerType = type?.arguments?.firstOrNull()?.type
            val innerValues = when (value) {
                is Iterable<*> -> value.map { item -> toJson(item, innerType, omitNulls) }
                is Sequence<*> -> value.map { item -> toJson(item, innerType, omitNulls) }.toList()
                else -> writingError("Expected Iterable or Sequence")
            }
            JsonArray(value = innerValues)
        }

        else -> {
            val mapper = type?.classifier?.getCompanionObject() as? JsonMapper<*>
            if (mapper != null) {
                mapper::toJson.allowAccessAndCall(value)
            } else {
                val properties = value::class.getPropertiesInDeclarationOrderIfPossible()
                val values = mutableMapOf<String, Json>()
                for (property in properties) {
                    val propertyValue = property.allowAccessAndCall(value)
                    if (propertyValue == null && omitNulls) continue
                    values[property.name] = toJson(propertyValue, property.returnType, omitNulls)
                }
                JsonObject(value = values)
            }
        }
    }
}

private fun writingError(message: String): Nothing {
    throw JsonWritingException(message)
}
