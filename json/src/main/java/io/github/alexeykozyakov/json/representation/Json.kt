package io.github.alexeykozyakov.json.representation

/**
 * JSON representation based on sealed classes.
 *
 *
 * [Json] -> [JsonObject]|[JsonArray]|[JsonString]|[JsonIntNumber]|[JsonFloatNumber]|[JsonBoolean]|[JsonNull]
 *
 * [JsonObject] -> map of [String] to [Json]
 *
 * [JsonArray] -> [List] of [Json]
 *
 * [JsonString] -> [String]
 *
 * [JsonIntNumber] -> [Int]
 *
 * [JsonFloatNumber] -> [Double]
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
 * JsonNumber of type int representation, contains int value.
 */
data class JsonIntNumber(
    /**
     * JSON int value.
     */
    val value: Int
) : Json

/**
 * JsonNumber of type float representation, contains floating point value.
 */
data class JsonFloatNumber(
    /**
     * JSON floating point value.
     */
    val value: Double
) : Json

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
