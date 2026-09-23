package io.github.alexeykozyakov.json.accessors

import io.github.alexeykozyakov.json.representation.Json
import io.github.alexeykozyakov.json.representation.JsonArray
import io.github.alexeykozyakov.json.representation.JsonBoolean
import io.github.alexeykozyakov.json.representation.JsonFloatNumber
import io.github.alexeykozyakov.json.representation.JsonIntNumber
import io.github.alexeykozyakov.json.representation.JsonNull
import io.github.alexeykozyakov.json.representation.JsonObject
import io.github.alexeykozyakov.json.representation.JsonString


/**
 * Interprets given [Json] as [JsonObject].
 * @throws IllegalStateException if JSON structure does not match expected type.
 */
fun Json.obj() = checkNotNull(this as? JsonObject) {
    "JsonObject object expected but was ${this::class.simpleName}"
}

/**
 * Interprets given [Json] as [JsonArray] and returns list of [Json] values.
 * @throws IllegalStateException if JSON structure does not match expected type.
 */
fun Json.array() = checkNotNull(this as? JsonArray) {
    "JsonArray expected but was ${this::class.simpleName}"
}.value

/**
 * Interprets given [Json] as [JsonString] and returns string value.
 * @throws IllegalStateException if JSON structure does not match expected type.
 */
fun Json.string() = checkNotNull(this as? JsonString) {
    "JsonString expected but was ${this::class.simpleName}"
}.value

/**
 * Interprets given [Json] as [JsonIntNumber] and returns int value.
 * @throws IllegalStateException if JSON structure does not match expected type.
 */
fun Json.int() = checkNotNull(this as? JsonIntNumber) {
    "JsonIntNumber expected but was ${this::class.simpleName}"
}.value

/**
 * Interprets given [Json] as [JsonFloatNumber] and returns float value.
 * @throws IllegalStateException if JSON structure does not match expected type.
 */
fun Json.float() = checkNotNull(this as? JsonFloatNumber) {
    "JsonFloatNumber expected but was ${this::class.simpleName}"
}.value

/**
 * Interprets given [Json] as [JsonBoolean] and returns boolean value.
 * @throws IllegalStateException if JSON structure does not match expected type.
 */
fun Json.boolean() =  checkNotNull(this as? JsonBoolean) {
    "JsonBoolean expected but was ${this::class.simpleName}"
}.value

/**
 * Returns null if provided [Json] is [JsonNull] or
 * interprets given [Json] as [JsonObject].
 * @throws IllegalStateException if JSON structure does not match expected type.
 */
fun Json.objOrNull() = if (this == JsonNull) null else obj()

/**
 * Returns null if provided [Json] is [JsonNull] or
 * interprets given [Json] as [JsonArray] and returns list of [Json] values.
 * @throws IllegalStateException if JSON structure does not match expected type
 */
fun Json.arrayOrNull() = if (this == JsonNull) null else array()

/**
 * Returns null if provided [Json] is [JsonNull] or
 * interprets given [Json] as [JsonString] and returns int value.
 * @throws IllegalStateException if JSON structure does not match expected type
 */
fun Json.stringOrNull() = if (this == JsonNull) null else string()

/**
 * Returns null if provided [Json] is [JsonNull] or
 * interprets given [Json] as [JsonIntNumber] and returns int value.
 * @throws IllegalStateException if JSON structure does not match expected type
 */
fun Json.intOrNull() = if (this == JsonNull) null else int()

/**
 * Returns null if provided [Json] is [JsonNull] or
 * interprets given [Json] as [JsonFloatNumber] and returns float value.
 * @throws IllegalStateException if JSON structure does not match expected type
 */
fun Json.floatOrNull() = if (this == JsonNull) null else float()

/**
 * Returns null if provided [Json] is [JsonNull] or
 * interprets given [Json] as [JsonBoolean] and returns boolean value.
 * @throws IllegalStateException if JSON structure does not match expected type
 */
fun Json.booleanOrNull() = if (this == JsonNull) null else boolean()

/**
 * Interprets given [Json] as [JsonObject] and
 * returns value of given key interpreted as [JsonObject].
 * @throws IllegalStateException if JSON structure does not match expected type
 */
fun Json.obj(key: String) = obj().require(key).obj()

/**
 * Interprets given [Json] as [JsonObject].
 * Interprets value of given key as [JsonArray].
 * Returns list of [Json] values of the array.
 * @throws IllegalStateException if JSON structure does not match expected type
 */
fun Json.array(key: String) = obj().require(key).array()

/**
 * Interprets given [Json] as [JsonObject].
 * Interprets value of given key as [JsonString].
 * Returns string value.
 * @throws IllegalStateException if JSON structure does not match expected type
 */
fun Json.string(key: String) = obj().require(key).string()

/**
 * Interprets given [Json] as [JsonObject].
 * Interprets value of given key as [JsonIntNumber].
 * Returns int value.
 * @throws IllegalStateException if JSON structure does not match expected type
 */
fun Json.int(key: String) = obj().require(key).int()

/**
 * Interprets given [Json] as [JsonObject].
 * Interprets value of given key as [JsonFloatNumber].
 * Returns float value.
 * @throws IllegalStateException if JSON structure does not match expected type
 */
fun Json.float(key: String) = obj().require(key).float()

/**
 * Interprets given [Json] as [JsonObject].
 * Interprets value of given key as [JsonBoolean].
 * Returns boolean value.
 * @throws IllegalStateException if JSON structure does not match expected type
 */
fun Json.boolean(key: String) = obj().require(key).boolean()

/**
 * Interprets given [Json] as [JsonObject] and
 * returns value of given key interpreted as [JsonObject]
 * or null if key is not exists or value is null.
 * @throws IllegalStateException if JSON structure does not match expected type
 */
fun Json.objOrNull(key: String) = obj()[key]?.objOrNull()

/**
 * Interprets given [Json] as [JsonObject].
 * Returns null if given key is not exists or value of given key is null.
 * Otherwise, interprets value of given key as [JsonArray]
 * and returns list of [Json].
 * @throws IllegalStateException if JSON structure does not match expected type
 */
fun Json.arrayOrNull(key: String) = obj()[key]?.arrayOrNull()

/**
 * Interprets given [Json] as [JsonObject].
 * Returns null if given key is not exists or value of given key is null.
 * Otherwise, interprets value of given key as [JsonString]
 * and returns string value of [JsonString].
 * @throws IllegalStateException if JSON structure does not match expected type
 */
fun Json.stringOrNull(key: String) = obj()[key]?.stringOrNull()

/**
 * Interprets given [Json] as [JsonObject].
 * Returns null if given key is not exists or value of given key is null.
 * Otherwise, interprets value of given key as JsonIntNumber
 * and returns int value of [JsonIntNumber].
 * @throws IllegalStateException if JSON structure does not match expected type
 */
fun Json.intOrNull(key: String) = obj()[key]?.intOrNull()

/**
 * Interprets given [Json] as [JsonObject].
 * Returns null if given key is not exists or value of given key is null.
 * Otherwise, interprets value of given key as [JsonFloatNumber]
 * and returns float value of [JsonFloatNumber].
 * @throws IllegalStateException if JSON structure does not match expected type
 */
fun Json.floatOrNull(key: String) = obj()[key]?.floatOrNull()

/**
 * Interprets given [Json] as [JsonObject].
 * Returns null if given key is not exists or value of given key is null.
 * Otherwise, interprets value of given key as [JsonBoolean]
 * and returns boolean value of [JsonBoolean].
 * @throws IllegalStateException if JSON structure does not match expected type
 */
fun Json.booleanOrNull(key: String) = obj()[key]?.booleanOrNull()

/**
 * Returns [Json] value of given [key].
 */
operator fun JsonObject.get(key: String) = value[key]

private fun JsonObject.require(key: String) =
    requireNotNull(value[key]) { "Value for key: $key is not provided" }

