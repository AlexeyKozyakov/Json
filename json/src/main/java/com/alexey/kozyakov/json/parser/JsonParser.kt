package com.alexey.kozyakov.json.parser

import com.alexey.kozyakov.json.representation.Json
import com.alexey.kozyakov.json.representation.JsonArray
import com.alexey.kozyakov.json.representation.JsonBoolean
import com.alexey.kozyakov.json.representation.JsonFloatNumber
import com.alexey.kozyakov.json.representation.JsonIntNumber
import com.alexey.kozyakov.json.representation.JsonNull
import com.alexey.kozyakov.json.representation.JsonObject
import com.alexey.kozyakov.json.representation.JsonString


/**
 * Parses [Json] from [input] string.
 */
fun parseJson(input: String): Json {
    val tokenizer = JsonTokenizer(input)
    val parser = Parser(tokenizer)
    return parser.parse()
}

internal class Parser(
    private val tokenizer: JsonTokenizer
) {
    fun parse(): Json {
        return parseObject()
            ?: parseArray()
            ?: parseString()
            ?: parseIntNumber()
            ?: parseFloatNumber()
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
                else -> {
                    if (token !is TokenString) tokenizer.parsingError("Key expected, but got: $token")
                    val colon = tokenizer.pop()
                    if (colon != TokenColon) tokenizer.parsingError("Colon expected, but got: $colon")
                    value[token.value] = parse()
                    when (val token = tokenizer.pop()) {
                        is TokenComma -> continue
                        is TokenClosePar -> break
                        else -> tokenizer.parsingError("Unexpected token: $token")
                    }
                }
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
                        else -> tokenizer.parsingError("Unexpected token: $token")
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

    private fun parseIntNumber(): JsonIntNumber? {
        val token = parseToken<TokenInt>() ?: return null
        return JsonIntNumber(value = token.value)
    }

    private fun parseFloatNumber(): JsonFloatNumber? {
        val token = parseToken<TokenFloat>() ?: return null
        return JsonFloatNumber(value = token.value)
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
