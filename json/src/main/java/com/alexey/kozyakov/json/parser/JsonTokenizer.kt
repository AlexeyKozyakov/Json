package com.alexey.kozyakov.json.parser

internal class JsonTokenizer(
    private val input: String
) {
    private var line = 0
    private var lineStart = 0
    private var position = 0
    private var currentToken: Token = TokenStart

    init {
        advance()
    }

    fun get(): Token {
        return currentToken
    }

    fun pop(): Token {
        val current = currentToken
        advance()
        return current
    }

    fun parsingError(message: String, errorPosition: Int = position): Nothing {
        error("Parsing error at position ${line + 1}:${errorPosition - lineStart + 1} $message")
    }

    private fun isEnded(): Boolean {
        return position >= input.length
    }

    private fun currentChar(): Char {
        return input[position]
    }

    private fun expectString(str: String) {
        val start = position
        position += str.length - 1
        if (isEnded()) parsingError("Expected $str, but EOF reached", start)
        val value = input.substring(start..position)
        position++
        if (value != str) parsingError("Expected $str, but got $value", start)
    }

    private fun advance() {
        while (!isEnded() && currentChar().isWhitespace()) {
            if (currentChar() == '\n') {
                line++
                lineStart = position + 1
            }
            position++
        }
        if (isEnded()) {
            currentToken = TokenEnd
            return
        }
        currentToken = when {
            currentChar() == '\"' -> {
                position++
                val start = position
                while (!isEnded() && currentChar() != '\"') position++
                if (isEnded()) parsingError("Expected \" but EOF reached", start)
                position++
                TokenString(value = input.substring(start until (position - 1)))
            }

            currentChar() == 't' -> {
                expectString("true")
                TokenBoolean(value = true)
            }

            currentChar() == 'f' -> {
                expectString("false")
                TokenBoolean(value = false)
            }

            currentChar() == 'n' -> {
                expectString("null")
                TokenNull
            }

            currentChar().isDigit()
                    || currentChar() == '-'
                    || currentChar() == '.' -> {
                val start = position
                while (!currentChar().isWhitespace()
                    && currentChar() != ','
                    && currentChar() != ']'
                    && currentChar() != '}'
                ) position++
                val value = input.substring(start until position)
                val parsed = value.toIntOrNull() ?: value.toDoubleOrNull()
                when (parsed) {
                    is Int -> TokenInt(value = parsed)
                    is Double -> TokenFloat(value = parsed)
                    else -> parsingError("Unsupported numeric value: $value", start)
                }
            }

            currentChar() == '{' -> {
                position++
                TokenOpenPar
            }

            currentChar() == '}' -> {
                position++
                TokenClosePar
            }

            currentChar() == '[' -> {
                position++
                TokenOpenBr
            }

            currentChar() == ']' -> {
                position++
                TokenCloseBr
            }

            currentChar() == ':' -> {
                position++
                TokenColon
            }

            currentChar() == ',' -> {
                position++
                TokenComma
            }

            else -> {
                parsingError("Unexpected token: ${currentChar()}")
            }
        }
    }
}

internal sealed interface Token

internal class TokenString(val value: String) : Token {
    override fun toString(): String {
        return "\"$value\""
    }
}

internal class TokenFloat(val value: Double) : Token {
    override fun toString(): String {
        return value.toString()
    }
}

internal class TokenInt(val value: Int) : Token {
    override fun toString(): String {
        return value.toString()
    }
}

internal class TokenBoolean(val value: Boolean) : Token {
    override fun toString(): String {
        return value.toString()
    }
}

internal object TokenNull : Token {
    override fun toString(): String {
        return "null"
    }
}

internal object TokenOpenBr : Token {
    override fun toString(): String {
        return "["
    }
}

internal object TokenCloseBr : Token {
    override fun toString(): String {
        return "]"
    }
}

internal object TokenOpenPar : Token {
    override fun toString(): String {
        return "("
    }
}

internal object TokenClosePar : Token {
    override fun toString(): String {
        return ")"
    }
}

internal object TokenComma : Token {
    override fun toString(): String {
        return ","
    }
}

internal object TokenColon : Token {
    override fun toString(): String {
        return ":"
    }
}

internal object TokenStart : Token {
    override fun toString(): String {
        return "start"
    }
}

internal object TokenEnd : Token {
    override fun toString(): String {
        return "EOF"
    }
}
