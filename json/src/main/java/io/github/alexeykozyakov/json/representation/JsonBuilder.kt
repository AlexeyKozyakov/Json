package io.github.alexeykozyakov.json.representation

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
 * Builds top-level JsonIntNumber with given [value].
 */
fun jsonInt(value: Int): JsonIntNumber {
    return JsonIntNumber(value)
}

/**
 * Builds top-level JsonFloatNumber with given [value].
 */
fun jsonFloat(value: Double): JsonFloatNumber {
    return JsonFloatNumber(value)
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
     * Adds field of type [JsonIntNumber] with given [value].
     */
    fun int(key: String, value: Int) {
        values[key] = JsonIntNumber(value)
    }

    /**
     * Adds field of type [JsonFloatNumber] with given [value].
     */
    fun float(key: String, value: Double) {
        values[key] = JsonFloatNumber(value)
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
     * Builds constructed [JsonObject].
     */
    fun build(): JsonObject {
        return JsonObject(values)
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
     * Adds to array value of type [JsonIntNumber] with given [value].
     */
    fun int(value: Int) {
        values += JsonIntNumber(value)
    }

    /**
     * Adds to array value of type [JsonFloatNumber] with given [value].
     */
    fun float(value: Double) {
        values += JsonFloatNumber(value)
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
     * Builds constructed [JsonArray].
     */
    fun build(): JsonArray {
        return JsonArray(values)
    }
}
