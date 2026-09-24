package io.github.alexeykozyakov.json.representation

/**
 * JSON representation based on sealed classes.
 *
 * JSON can be one of the following:
 *  - object
 *  - array
 *  - string
 *  - number
 *  - boolean
 *  - null
 */
sealed interface Json

/**
 * JSON object representation. Contains map of object fields.
 */
data class JsonObject(
    /**
     * JSON object fields map.
     */
    val value: Map<String, Json>
) : Json

/**
 * JSON array representation. Contains list of array values.
 */
data class JsonArray(
    /**
     * JSON array values list.
     */
    val value: List<Json>
) : Json

/**
 * JSON string representation, contains string value.
 */
data class JsonString(
    /**
     * JSON string value.
     */
    val value: String
) : Json

/**
 * JSON number representation, contains numeric value.
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
 * JSON boolean representation, contains boolean value.
 */
data class JsonBoolean(
    /**
     * JSON boolean value.
     */
    val value: Boolean
) : Json

/**
 * JSON null representation
 */
data object JsonNull : Json
