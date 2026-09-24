package io.github.alexeykozyakov.json.builder

import io.github.alexeykozyakov.json.representation.Json
import io.github.alexeykozyakov.json.representation.JsonArray
import io.github.alexeykozyakov.json.representation.JsonBoolean
import io.github.alexeykozyakov.json.representation.JsonNull
import io.github.alexeykozyakov.json.representation.JsonNumber
import io.github.alexeykozyakov.json.representation.JsonObject
import io.github.alexeykozyakov.json.representation.JsonString

/**
 * Builds top-level JsonObject using [JsonObjectBuilder] api.
 */
fun jsonObj(builder: JsonObjectBuilder.() -> Unit): JsonObject {
    return JsonObjectBuilder().apply(builder).build()
}

/**
 * Builds top-level JsonArray using [JsonArrayBuilder] api.
 */
fun jsonArray(builder: JsonArrayBuilder.() -> Unit): JsonArray {
    return JsonArrayBuilder().apply(builder).build()
}

/**
 * Builds top-level JsonString with given [value].
 */
fun jsonString(value: String): JsonString {
    return JsonString(value)
}

/**
 * Builds top-level JsonNumber with given [value].
 */
fun jsonNumber(value: Number): JsonNumber {
    return JsonNumber(value)
}

/**
 * Builds top-level JsonNumber with given [Long] [value].
 */
fun jsonLong(value: Long): JsonNumber {
    return JsonNumber(value)
}

/**
 * Builds top-level JsonNumber with given [Int] [value].
 */
fun jsonInt(value: Int): JsonNumber {
    return JsonNumber(value)
}

/**
 * Builds top-level JsonNumber with given [Short] [value].
 */
fun jsonShort(value: Short): JsonNumber {
    return JsonNumber(value)
}

/**
 * Builds top-level JsonNumber with given [Byte] [value].
 */
fun jsonByte(value: Byte): JsonNumber {
    return JsonNumber(value)
}

/**
 * Builds top-level JsonNumber with given [Double] [value].
 */
fun jsonDouble(value: Double): JsonNumber {
    return JsonNumber(value)
}

/**
 * Builds top-level JsonFloatNumber with given [Float] [value].
 */
fun jsonFloat(value: Float): JsonNumber {
    return JsonNumber(value)
}

/**
 * Builds top-level JsonBoolean with given [value].
 */
fun jsonBoolean(value: Boolean): JsonBoolean {
    return JsonBoolean(value)
}

/**
 * Returns [JsonNull] object.
 */
fun jsonNull(): JsonNull {
    return JsonNull
}

/**
 * Provides api to build [JsonObject] using dsl.
 */
class JsonObjectBuilder {
    private val values = mutableMapOf<String, Json>()

    /**
     * Adds field of type [JsonObject] with [key] and value constructed by [JsonObjectBuilder].
     */
    fun obj(key: String, builder: JsonObjectBuilder.() -> Unit) {
        values[key] = JsonObjectBuilder().apply(builder).build()
    }

    /**
     * Adds field of type [JsonArray] constructed by [JsonArrayBuilder].
     */
    fun array(key: String, builder: JsonArrayBuilder.() -> Unit) {
        values[key] = JsonArrayBuilder().apply(builder).build()
    }

    /**
     * Adds field of type [JsonString] with given [value].
     */
    fun string(key: String, value: String) {
        values[key] = JsonString(value)
    }

    /**
     * Adds field of type [JsonNumber] with given [Number] [value].
     */
    fun number(key: String, value: Number) {
        values[key] = JsonNumber(value)
    }

    /**
     * Adds field of type [JsonNumber] with given [Long] [value].
     */
    fun long(key: String, value: Long) {
        values[key] = JsonNumber(value)
    }

    /**
     * Adds field of type [JsonNumber] with given [Int] [value].
     */
    fun int(key: String, value: Int) {
        values[key] = JsonNumber(value)
    }

    /**
     * Adds field of type [JsonNumber] with given [Short] [value].
     */
    fun short(key: String, value: Short) {
        values[key] = JsonNumber(value)
    }

    /**
     * Adds field of type [JsonNumber] with given [Byte] [value].
     */
    fun byte(key: String, value: Byte) {
        values[key] = JsonNumber(value)
    }

    /**
     * Adds field of type [JsonNumber] with given [Double] [value].
     */
    fun double(key: String, value: Double) {
        values[key] = JsonNumber(value)
    }

    /**
     * Adds field of type [JsonNumber] with given [Float] [value].
     */
    fun float(key: String, value: Float) {
        values[key] = JsonNumber(value)
    }

    /**
     * Adds field of type [JsonBoolean] with given [value].
     */
    fun boolean(key: String, value: Boolean) {
        values[key] = JsonBoolean(value)
    }

    /**
     * Adds [JsonNull] field.
     */
    fun nul(key: String) {
        values[key] = JsonNull
    }

    /**
     * Adds arbitrary [Json] field.
     */
    fun json(key: String, value: Json) {
        values[key] = value
    }

    /**
     * Adds all fields from given [JsonObject].
     */
    fun fields(obj: JsonObject) {
        values += obj.value
    }

    /**
     * Builds constructed [JsonObject].
     */
    fun build(): JsonObject {
        return JsonObject(values.toMap())
    }
}

/**
 * Provides api to build [JsonArray] using dsl.
 */
class JsonArrayBuilder {
    private val values = mutableListOf<Json>()

    /**
     * Adds to array value of type [JsonObject] constructed using [JsonObjectBuilder].
     */
    fun obj(builder: JsonObjectBuilder.() -> Unit) {
        values += JsonObjectBuilder().apply(builder).build()
    }

    /**
     * Adds to array value of type [JsonArray] constructed using [JsonArrayBuilder].
     */
    fun array(builder: JsonArrayBuilder.() -> Unit) {
        values += JsonArrayBuilder().apply(builder).build()
    }

    /**
     * Adds to array value of type [JsonString] with given [value].
     */
    fun string(value: String) {
        values += JsonString(value)
    }

    /**
     * Adds to array value of type [JsonNumber] with given [Number] [value].
     */
    fun number(value: Number) {
        values += JsonNumber(value)
    }

    /**
     * Adds to array value of type [JsonNumber] with given [Long] [value].
     */
    fun long(value: Long) {
        values += JsonNumber(value)
    }

    /**
     * Adds to array value of type [JsonNumber] with given [Int] [value].
     */
    fun int(value: Int) {
        values += JsonNumber(value)
    }

    /**
     * Adds to array value of type [JsonNumber] with given [Short] [value].
     */
    fun short(value: Short) {
        values += JsonNumber(value)
    }

    /**
     * Adds to array value of type [JsonNumber] with given [Byte] [value].
     */
    fun byte(value: Byte) {
        values += JsonNumber(value)
    }

    /**
     * Adds to array value of type [JsonNumber] with given [Double] [value].
     */
    fun double(value: Double) {
        values += JsonNumber(value)
    }

    /**
     * Adds to array value of type [JsonNumber] with given [Float] [value].
     */
    fun float(value: Float) {
        values += JsonNumber(value)
    }

    /**
     * Adds to array value of type [JsonBoolean] with given [value].
     */
    fun boolean(value: Boolean) {
        values += JsonBoolean(value)
    }

    /**
     * Adds [JsonNull] to array.
     */
    fun nul() {
        values += JsonNull
    }

    /**
     * Adds arbitrary [Json] to array.
     */
    fun json(json: Json) {
        values += json
    }

    /**
     * Builds constructed [JsonArray].
     */
    fun build(): JsonArray {
        return JsonArray(values.toList())
    }
}
