package io.github.alexeykozyakov.json.representation

/**
 * JSON representation based on sealed classes.
 *
 *
 * [Json] -> [JsonObject]|[JsonArray]|[JsonString]|[JsonNumber]|[JsonBoolean]|[JsonNull]
 *
 * [JsonObject] -> map of [String] to [Json]
 *
 * [JsonArray] -> [List] of [Json]
 *
 * [JsonString] -> [String]
 *
 * [JsonNumber] -> [Number]
 *
 * [JsonBoolean] -> [Boolean]
 *
 * [JsonNull] -> null
 */
sealed interface Json

/**
 * JsonObject representation. Contains map of Json properties.
 */
data class JsonObject(
    /**
     * JSON object values map.
     */
    val value: Map<String, Json>
) : Json

/**
 * JsonArray representation. Contains list of Json values.
 */
data class JsonArray(
    /**
     * JSON array values list.
     */
    val value: List<Json>
) : Json

/**
 * JsonString representation, contains string value.
 */
data class JsonString(
    /**
     * JSON string value.
     */
    val value: String
) : Json

/**
 * JsonNumber representation, contains numeric value.
 */
data class JsonNumber(
    /**
     * JSON number value.
     */
    val value: Number
) : Json {
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is JsonNumber) return false
        if (!value.isFloatingPoint() && !other.value.isFloatingPoint()) {
            return value.toLong() == other.value.toLong()
        }
        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    private fun Number.isFloatingPoint(): Boolean {
        return this is Double || this is Float
    }
}

/**
 * JsonNumber of type boolean representation, contains boolean value.
 */
data class JsonBoolean(
    /**
     * JSON boolean value.
     */
    val value: Boolean
) : Json

/**
 * JsonNull representation
 */
data object JsonNull : Json
