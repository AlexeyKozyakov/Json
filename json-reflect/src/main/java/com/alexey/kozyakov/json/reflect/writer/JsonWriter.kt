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

fun Any?.toJson(): String {
    val json = toJson(this)
    return writeJson(json)
}

private fun toJson(value: Any?): Json {
    return when (value) {
        is String -> JsonString(value)

        is Int -> JsonIntNumber(value)

        is Double -> JsonFloatNumber(value)

        is Boolean -> JsonBoolean(value)

        null -> JsonNull

        is List<*> -> {
            val values = value.map { item -> toJson(item) }
            JsonArray(value = values)
        }

        else -> {
            val properties = value::class.declaredMemberProperties
            val values = properties.associate { property ->
                val propertyValue = property.call(value)
                property.name to toJson(propertyValue)
            }
            JsonObject(value = values)
        }
    }
}
