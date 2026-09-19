package com.alexey.kozyakov.json.writer

import com.alexey.kozyakov.json.representation.Json
import com.alexey.kozyakov.json.representation.JsonArray
import com.alexey.kozyakov.json.representation.JsonBoolean
import com.alexey.kozyakov.json.representation.JsonFloatNumber
import com.alexey.kozyakov.json.representation.JsonIntNumber
import com.alexey.kozyakov.json.representation.JsonNull
import com.alexey.kozyakov.json.representation.JsonObject
import com.alexey.kozyakov.json.representation.JsonString

/**
 * Writes [json] to string.
 * If [pretty] is set to true new lines and whitespaces will be added to output.
 */
fun writeJson(json: Json, pretty: Boolean = true): String {
    return buildString {
        writeJson(json, pretty, level = 0)
    }
}

private fun StringBuilder.writeJson(json: Json, pretty: Boolean, level: Int) {
    when (json) {
        is JsonObject -> writeObject(json, pretty, level)
        is JsonArray -> writeArray(json, pretty, level)
        is JsonString -> append("\"${json.value}\"")
        is JsonIntNumber -> append(json.value)
        is JsonFloatNumber -> append(json.value)
        is JsonBoolean -> append(json.value)
        JsonNull -> append("null")
    }
}

private fun StringBuilder.writeObject(json: JsonObject, pretty: Boolean, level: Int) {
    val indent by lazy { String(CharArray(level * 4) { ' ' }) }
    append('{')
    if (pretty) append("\n")
    json.value.entries.forEachIndexed { index, field ->
        if (pretty) append("    $indent")
        append("\"${field.key}\":")
        if (pretty) append(" ")
        writeJson(field.value, pretty, level + 1)
        if (index < json.value.size - 1) {
            append(',')
        }
        if (pretty) append('\n')
    }
    if (pretty) append(indent)
    append('}')
}

private fun StringBuilder.writeArray(json: JsonArray, pretty: Boolean, level: Int) {
    val indent by lazy { String(CharArray(level * 4) { ' ' }) }
    append('[')
    if (pretty) append('\n')
    json.value.forEachIndexed { index, value ->
        if (pretty) append("    $indent")
        writeJson(value, pretty, level + 1)
        if (index < json.value.size - 1) {
            append(',')
        }
        if (pretty) append('\n')
    }
    if (pretty) append(indent)
    append(']')
}
