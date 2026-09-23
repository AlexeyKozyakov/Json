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
 * Interprets given [Json] as [JsonObject]
 */
fun Json.obj() = checkNotNull(this as? JsonObject) { "Json object expected" }

/**
 * Interprets given [Json] as [JsonArray] and returns list of [Json] values
 */
fun Json.array() = checkNotNull(this as? JsonArray) { "Json array expected" }.value

/**
 * Interprets given [Json] as [JsonString] and returns string value
 */
fun Json.string() = checkNotNull(this as? JsonString) { "String value expected" }.value

/**
 * Interprets given [Json] as [JsonIntNumber] and returns int value
 */
fun Json.int() = checkNotNull(this as? JsonIntNumber) { "Int value expected" }.value

/**
 * Interprets given [Json] as [JsonFloatNumber] and returns float value
 */
fun Json.float() = checkNotNull(this as? JsonFloatNumber) { "Float value expected" }.value

/**
 * Interprets given [Json] as [JsonBoolean] and returns boolean value
 */
fun Json.boolean() =  checkNotNull(this as? JsonBoolean) { "Boolean value expected" }.value

/**
 * Interprets given [Json] as [JsonObject] or null.
 */
fun Json.objOrNull() = if (this == JsonNull) null else obj()

/**
 * Returns null if provided [Json] is [JsonNull] or
 * interprets given [Json] as [JsonObject].
 */
fun Json.arrayOrNull() = if (this == JsonNull) null else array()

/**
 * Returns null if provided [Json] is [JsonNull] or
 * interprets given [Json] as [JsonArray] and returns list of [Json] values.
 */
fun Json.stringOrNull() = if (this == JsonNull) null else string()

/**
 * Returns null if provided [Json] is [JsonNull] or
 * interprets given [Json] as [JsonIntNumber] and returns int value.
 */
fun Json.intOrNull() = if (this == JsonNull) null else int()

/**
 * Returns null if provided [Json] is [JsonNull] or
 * interprets given [Json] as [JsonFloatNumber] and returns float value.
 */
fun Json.floatOrNull() = if (this == JsonNull) null else float()

/**
 * Returns null if provided [Json] is [JsonNull] or
 * interprets given [Json] as [JsonBoolean] and returns boolean value.
 */
fun Json.booleanOrNull() = if (this == JsonNull) null else boolean()

/**
 * Interprets given [Json] as [JsonObject] and
 * returns value of given key interpreted as [JsonObject].
 */
fun Json.obj(key: String) = obj().require(key).obj()

/**
 * Interprets given [Json] as [JsonObject].
 * Interprets value of given key as [JsonArray].
 * Returns list of [Json] values of the array.
 */
fun Json.array(key: String) = obj().require(key).array()

/**
 * Interprets given [Json] as [JsonObject].
 * Interprets value of given key as [JsonString].
 * Returns string value.
 */
fun Json.string(key: String) = obj().require(key).string()

/**
 * Interprets given [Json] as [JsonObject].
 * Interprets value of given key as [JsonIntNumber].
 * Returns int value.
 */
fun Json.int(key: String) = obj().require(key).int()

/**
 * Interprets given [Json] as [JsonObject].
 * Interprets value of given key as [JsonFloatNumber].
 * Returns float value.
 */
fun Json.float(key: String) = obj().require(key).float()

/**
 * Interprets given [Json] as [JsonObject].
 * Interprets value of given key as [JsonBoolean].
 * Returns boolean value.
 */
fun Json.boolean(key: String) = obj().require(key).boolean()

/**
 * Interprets given [Json] as [JsonObject] and
 * returns value of given key interpreted as [JsonObject]
 * or null if key is not exists or value is null.
 */
fun Json.objOrNull(key: String) = obj().get(key)?.objOrNull()

/**
 * Interprets given [Json] as [JsonObject].
 * Returns null if given key is not exists or value of given key is null.
 * Otherwise, interprets value of given key as [JsonArray]
 * and returns list of [Json].
 */
fun Json.arrayOrNull(key: String) = obj().get(key)?.arrayOrNull()

/**
 * Interprets given [Json] as [JsonObject].
 * Returns null if given key is not exists or value of given key is null.
 * Otherwise, interprets value of given key as [JsonString]
 * and returns string value of [JsonString].
 */
fun Json.stringOrNull(key: String) = obj().get(key)?.stringOrNull()

/**
 * Interprets given [Json] as [JsonObject].
 * Returns null if given key is not exists or value of given key is null.
 * Otherwise, interprets value of given key as JsonIntNumber
 * and returns int value of [JsonIntNumber].
 */
fun Json.intOrNull(key: String) = obj().get(key)?.intOrNull()

/**
 * Interprets given [Json] as [JsonObject].
 * Returns null if given key is not exists or value of given key is null.
 * Otherwise, interprets value of given key as [JsonFloatNumber]
 * and returns float value of [JsonFloatNumber].
 */
fun Json.floatOrNull(key: String) = obj().get(key)?.floatOrNull()

/**
 * Interprets given [Json] as [JsonObject].
 * Returns null if given key is not exists or value of given key is null.
 * Otherwise, interprets value of given key as [JsonBoolean]
 * and returns boolean value of [JsonBoolean].
 */
fun Json.booleanOrNull(key: String) = obj().get(key)?.booleanOrNull()

private fun JsonObject.require(key: String) =
    requireNotNull(value[key]) { "Value for key: $key is not provided" }

private fun JsonObject.get(key: String) = value[key]
