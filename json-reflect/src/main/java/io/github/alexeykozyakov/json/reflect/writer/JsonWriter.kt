package io.github.alexeykozyakov.json.reflect.writer

import io.github.alexeykozyakov.json.representation.Json
import io.github.alexeykozyakov.json.representation.JsonArray
import io.github.alexeykozyakov.json.representation.JsonBoolean
import io.github.alexeykozyakov.json.representation.JsonNull
import io.github.alexeykozyakov.json.representation.JsonNumber
import io.github.alexeykozyakov.json.representation.JsonObject
import io.github.alexeykozyakov.json.representation.JsonString
import io.github.alexeykozyakov.json.writer.writeJson
import kotlin.reflect.full.declaredMemberProperties

/**
 * Writes value of type T to JSON string.
 *
 * T can be one of the following:
 *
 * T <- String, Number, Boolean
 *
 * T <- null
 *
 * T <- Iterable<T>, Collection<T>, List<T>
 *
 * T <- Sequence<T>
 *
 * T <- class { val t1: T1, val t2: T2, ... }
 *
 * @param omitNulls enables omitting of null properties during class serialization.
 */
fun Any?.toJson(omitNulls: Boolean = true): String {
    val json = toJson(this, omitNulls)
    return writeJson(json)
}

private fun toJson(value: Any?, omitNulls: Boolean): Json {
    return when (value) {
        is String -> JsonString(value)

        is Number -> JsonNumber(value)

        is Boolean -> JsonBoolean(value)

        null -> JsonNull

        is Char -> error("Unsupported primitive type Char")

        is Iterable<*> -> {
            val values = value.map { item -> toJson(item, omitNulls) }
            JsonArray(value = values)
        }

        is Sequence<*> -> toJson(value.asIterable(), omitNulls)

        else -> {
            val properties = value::class.declaredMemberProperties
            val values = mutableMapOf<String, Json>()
            for (property in properties) {
                val propertyValue = property.call(value)
                if (propertyValue == null && omitNulls) continue
                values[property.name] = toJson(propertyValue, omitNulls)
            }
            JsonObject(value = values)
        }
    }
}
