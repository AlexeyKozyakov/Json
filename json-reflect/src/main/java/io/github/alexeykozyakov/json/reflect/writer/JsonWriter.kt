package io.github.alexeykozyakov.json.reflect.writer

import io.github.alexeykozyakov.json.representation.Json
import io.github.alexeykozyakov.json.representation.JsonArray
import io.github.alexeykozyakov.json.representation.JsonBoolean
import io.github.alexeykozyakov.json.representation.JsonFloatNumber
import io.github.alexeykozyakov.json.representation.JsonIntNumber
import io.github.alexeykozyakov.json.representation.JsonNull
import io.github.alexeykozyakov.json.representation.JsonObject
import io.github.alexeykozyakov.json.representation.JsonString
import io.github.alexeykozyakov.json.writer.writeJson
import kotlin.reflect.full.declaredMemberProperties

/**
 * Writes value of any type to json string.
 */
fun Any?.toJson(omitNulls: Boolean = true): String {
    val json = toJson(this, omitNulls)
    return writeJson(json)
}

private fun toJson(value: Any?, omitNulls: Boolean): Json {
    return when (value) {
        is String -> JsonString(value)

        is Int -> JsonIntNumber(value)

        is Double -> JsonFloatNumber(value)

        is Boolean -> JsonBoolean(value)

        null -> JsonNull

        is Byte,
        is Short,
        is Long,
        is Float,
        is Char -> error("Unsupported primitive type: ${value::class.simpleName}")

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
