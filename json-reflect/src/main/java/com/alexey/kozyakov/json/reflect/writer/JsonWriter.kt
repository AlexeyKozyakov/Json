package com.alexey.kozyakov.json.reflect.writer

import com.alexey.kozyakov.json.representation.Json
import com.alexey.kozyakov.json.representation.JsonArray
import com.alexey.kozyakov.json.representation.JsonBoolean
import com.alexey.kozyakov.json.representation.JsonFloatNumber
import com.alexey.kozyakov.json.representation.JsonIntNumber
import com.alexey.kozyakov.json.representation.JsonNull
import com.alexey.kozyakov.json.representation.JsonObject
import com.alexey.kozyakov.json.representation.JsonString
import com.alexey.kozyakov.json.writer.writeJson
import kotlin.reflect.full.declaredMemberProperties

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
