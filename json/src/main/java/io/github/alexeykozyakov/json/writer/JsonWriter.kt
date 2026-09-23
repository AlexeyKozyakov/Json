package io.github.alexeykozyakov.json.writer

import io.github.alexeykozyakov.json.representation.Json
import io.github.alexeykozyakov.json.representation.JsonArray
import io.github.alexeykozyakov.json.representation.JsonBoolean
import io.github.alexeykozyakov.json.representation.JsonFloatNumber
import io.github.alexeykozyakov.json.representation.JsonIntNumber
import io.github.alexeykozyakov.json.representation.JsonNull
import io.github.alexeykozyakov.json.representation.JsonObject
import io.github.alexeykozyakov.json.representation.JsonString

/**
 * Writes provided [json] to string.
 *
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
        is JsonString -> writeString(json)
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

private fun StringBuilder.writeString(json: JsonString) {
    val escaped = buildString {
        for (char in json.value) {
            when (char) {
                '\"', '\\', '/' -> append("\\$char")
                '\b' -> append("\\b")
                0x0C.toChar() -> append("\\f")
                '\n' -> append("\\n")
                '\r' -> append("\\r")
                '\t' -> append("\\t")
                else -> if (char in 0.toChar()..0x001F.toChar()) {
                    val encoded = char.code
                        .toString(radix = 16)
                        .padStart(length = 4, padChar = '0')
                    append("\\u${encoded}")
                } else {
                    append(char)
                }
            }
        }
    }
    append("\"$escaped\"")
}
