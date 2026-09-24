package io.github.alexeykozyakov.json.parser

import io.github.alexeykozyakov.json.representation.Json
import io.github.alexeykozyakov.json.representation.JsonArray
import io.github.alexeykozyakov.json.representation.JsonBoolean
import io.github.alexeykozyakov.json.representation.JsonNull
import io.github.alexeykozyakov.json.representation.JsonNumber
import io.github.alexeykozyakov.json.representation.JsonObject
import io.github.alexeykozyakov.json.representation.JsonString
import javax.xml.transform.Source


/**
 * Parses JSON from [input] string.
 * Returns [Json] sealed interface.
 *
 * For example:
 * parseJson(""" "name": "Alexey", "age": 28 """)
 * will return [JsonObject] instance which contains
 * [JsonString] and [JsonNumber] in the values map.
 *
 * JSON properties can be accessed by json.string("name"), json.int("age")
 * accessors.
 *
 * @throws JsonParsingException if parsing error is occurred
 */
fun parseJson(input: String): Json {
    val tokenizer = JsonTokenizer(input)
    val parser = Parser(tokenizer)
    return parser.parse()
}

/**
 * Exception which can be thrown while JSON parsing if
 * its syntax or structure is incorrect.
 */
class JsonParsingException(message: String, cause: Exception? = null) :
    IllegalArgumentException(message, cause)

internal class Parser(
    private val tokenizer: JsonTokenizer
) {
    fun parse(): Json {
        return parseObject()
            ?: parseArray()
            ?: parseString()
            ?: parseNumber()
            ?: parseBoolean()
            ?: parseNull()
            ?: tokenizer.parsingError("Unexpected token: ${tokenizer.get()}")
    }

    private fun parseObject(): JsonObject? {
        parseToken<TokenOpenPar>() ?: return null
        val value = mutableMapOf<String, Json>()
        while (true) {
            when (val token = tokenizer.pop()) {
                is TokenClosePar -> break
                is TokenString -> {
                    parseToken<TokenColon>() ?: tokenizer.parsingError("Colon expected")
                    value[token.value] = parse()
                    when (val token = tokenizer.pop()) {
                        is TokenComma -> continue
                        is TokenClosePar -> break
                        else -> tokenizer.parsingError(", or } expected, but got: $token")
                    }
                }

                else -> tokenizer.parsingError("Key or } expected, but got: $token")
            }
        }
        return JsonObject(value = value)
    }

    private fun parseArray(): JsonArray? {
        parseToken<TokenOpenBr>() ?: return null
        val value = mutableListOf<Json>()
        while (true) {
            when (tokenizer.get()) {
                is TokenCloseBr -> {
                    tokenizer.pop()
                    break
                }

                else -> {
                    value.add(parse())
                    when (val token = tokenizer.pop()) {
                        is TokenComma -> continue
                        is TokenCloseBr -> break
                        else -> tokenizer.parsingError(", or ] expected, but got: $token")
                    }
                }
            }
        }
        return JsonArray(value = value)
    }

    private fun parseString(): JsonString? {
        val token = parseToken<TokenString>() ?: return null
        return JsonString(value = token.value)
    }

    private fun parseNumber(): JsonNumber? {
        val token = parseToken<TokenNumber>() ?: return null
        return JsonNumber(value = token.value)
    }

    private fun parseBoolean(): JsonBoolean? {
        val token = parseToken<TokenBoolean>() ?: return null
        return JsonBoolean(value = token.value)
    }

    private fun parseNull(): JsonNull? {
        parseToken<TokenNull>() ?: return null
        return JsonNull
    }

    private inline fun <reified T : Any> parseToken(): T? {
        val token = tokenizer.get()
        if (token is T) {
            tokenizer.pop()
            return token
        }
        return null
    }
}
