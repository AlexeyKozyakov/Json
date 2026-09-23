package io.github.alexeykozyakov.json.parser

internal class JsonTokenizer(
    private val input: String
) {
    private var line = 0
    private var lineStart = 0
    private var position = 0
    private var currentToken: Token = TokenStart

    fun get(): Token {
        if (currentToken == TokenStart) {
            advance()
        }
        return currentToken
    }

    fun pop(): Token {
        val current = get()
        advance()
        return current
    }

    fun parsingError(message: String, errorPosition: Int = position): Nothing {
        error("Parsing error at position ${line + 1}:${errorPosition - lineStart + 1} $message")
    }

    private fun advance() {
        skipWhitespaces()

        if (isEnded()) {
            currentToken = TokenEnd
            return
        }

        currentToken = when (currentChar()) {
            '\"' -> {
                val string = parseStringLiteral()
                TokenString(value = string)
            }

            't' -> {
                expectString("true")
                TokenBoolean(value = true)
            }

            'f' -> {
                expectString("false")
                TokenBoolean(value = false)
            }

            'n' -> {
                expectString("null")
                TokenNull
            }

            '{' -> {
                position++
                TokenOpenPar
            }

            '}' -> {
                position++
                TokenClosePar
            }

            '[' -> {
                position++
                TokenOpenBr
            }

            ']' -> {
                position++
                TokenCloseBr
            }

            ':' -> {
                position++
                TokenColon
            }

            ',' -> {
                position++
                TokenComma
            }

            else -> if (currentChar().isDigit() || currentChar() == '-' || currentChar() == '.') {
                val start = position
                when (val number = parseNumber()) {
                    is Int -> TokenInt(value = number)
                    is Double -> TokenFloat(value = number)
                    else -> parsingError("Unsupported numeric value: $number", start)
                }
            } else {
                parsingError("Unexpected character: ${currentChar()}")
            }
        }
    }

    private fun skipWhitespaces() {
        while (!isEnded() && currentChar().isWhitespace()) {
            if (currentChar() == '\n') {
                line++
                lineStart = position + 1
            }
            position++
        }
    }

    private fun currentChar(): Char {
        return input[position]
    }

    private fun isEnded(): Boolean {
        return position >= input.length
    }

    private fun expectString(str: String) {
        val start = position
        position += str.length - 1
        if (isEnded()) parsingError("Expected $str, but EOF reached", start)
        val value = input.substring(start..position)
        position++
        if (value != str) parsingError("Expected $str, but got $value", start)
    }

    private fun parseStringLiteral(): String {
        position++
        return buildString {
            while (!isEnded() && currentChar() != '\"') {
                if (currentChar() == '\\') {
                    position++
                    if (isEnded()) parsingError("Unexpected EOF")
                    when (currentChar()) {
                        '\"', '\\', '/' -> append(currentChar())
                        'b' -> append('\b')
                        'f' -> append(0x0C.toChar())
                        'n' -> append('\n')
                        'r' -> append('\r')
                        't' -> append('\t')
                        'u' -> {
                            position++
                            val hexString = buildString {
                                var count = 0
                                while (!isEnded() && count < 4 && currentChar().isHexDigit()) {
                                    append(currentChar())
                                    position++
                                    count++
                                }
                            }
                            position--
                            append(hexString.toInt(radix = 16).toChar())
                        }
                    }
                } else {
                    append(currentChar())
                }
                position++
            }
            if (isEnded()) parsingError("Expected \" but EOF reached")
            position++
        }
    }

    private fun parseNumber(): Number? {
        val start = position
        while (!isEnded()
            && !currentChar().isWhitespace()
            && currentChar() != ','
            && currentChar() != ']'
            && currentChar() != '}'
        ) position++
        val value = input.substring(start until position)
        return value.toIntOrNull() ?: value.toDoubleOrNull()
    }

    private fun Char.isHexDigit(): Boolean {
        return this in 'a'..'f' || this in 'A'..'F' || this.isDigit()
    }
}

internal sealed interface Token

internal class TokenString(val value: String) : Token {
    override fun toString() = "\"$value\""
}

internal class TokenFloat(val value: Double) : Token {
    override fun toString() = value.toString()
}

internal class TokenInt(val value: Int) : Token {
    override fun toString() = value.toString()
}

internal class TokenBoolean(val value: Boolean) : Token {
    override fun toString() = value.toString()
}

internal object TokenNull : Token {
    override fun toString() = "null"
}

internal object TokenOpenBr : Token {
    override fun toString() = "["
}

internal object TokenCloseBr : Token {
    override fun toString() = "]"
}

internal object TokenOpenPar : Token {
    override fun toString() = "("
}

internal object TokenClosePar : Token {
    override fun toString() = ")"
}

internal object TokenComma : Token {
    override fun toString() = ","
}

internal object TokenColon : Token {
    override fun toString() = ":"
}

internal object TokenStart : Token {
    override fun toString() = "SOF"
}

internal object TokenEnd : Token {
    override fun toString() = "EOF"
}
