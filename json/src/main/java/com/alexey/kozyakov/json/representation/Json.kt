package com.alexey.kozyakov.json.representation

/**
 * Json representation.
 *
 * [Json] -> [JsonObject]|[JsonArray]|[JsonString]|[JsonIntNumber]|[JsonFloatNumber]|[JsonBoolean]|[JsonNull]
 * [JsonObject] -> map of [String] to [Json]
 * [JsonArray] -> [List] of [Json]
 * [JsonString] -> [String]
 * [JsonIntNumber] -> [Int]
 * [JsonFloatNumber] -> [Double]
 * [JsonBoolean] -> [Boolean]
 * [JsonNull] -> null
 */
sealed interface Json

/**
 * JsonObject representation. Contains map of Json properties.
 */
data class JsonObject(val value: Map<String, Json>) : Json

/**
 * JsonArray representation. Contains list of Json values.
 */
data class JsonArray(val value: List<Json>) : Json

/**
 * JsonString representation, contains string value.
 */
data class JsonString(val value: String) : Json

/**
 * JsonNumber of type int representation, contains int value.
 */
data class JsonIntNumber(val value: Int) : Json

/**
 * JsonNumber of type float representation, contains floating point value.
 */
data class JsonFloatNumber(val value: Double) : Json

/**
 * JsonNumber of type boolean representation, contains boolean value.
 */
data class JsonBoolean(val value: Boolean) : Json

/**
 * JsonNull representation
 */
data object JsonNull : Json { val value = null }
