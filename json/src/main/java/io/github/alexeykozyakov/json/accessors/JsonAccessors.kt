package io.github.alexeykozyakov.json.accessors

import io.github.alexeykozyakov.json.representation.Json
import io.github.alexeykozyakov.json.representation.JsonArray
import io.github.alexeykozyakov.json.representation.JsonBoolean
import io.github.alexeykozyakov.json.representation.JsonNull
import io.github.alexeykozyakov.json.representation.JsonNumber
import io.github.alexeykozyakov.json.representation.JsonObject
import io.github.alexeykozyakov.json.representation.JsonString


/**
 * Interprets given [Json] as [JsonObject].
 * @throws IllegalArgumentException if JSON structure does not match expected type.
 */
fun Json.obj() = requireNotNull(this as? JsonObject) {
    "JsonObject expected but was ${this::class.simpleName}"
}

/**
 * Interprets given [Json] as [JsonArray] and returns list of [Json] values.
 * @throws IllegalArgumentException if JSON structure does not match expected type.
 */
fun Json.array() = requireNotNull(this as? JsonArray) {
    "JsonArray expected but was ${this::class.simpleName}"
}.value

/**
 * Interprets given [Json] as [JsonString] and returns string value.
 * @throws IllegalArgumentException if JSON structure does not match expected type.
 */
fun Json.string() = requireNotNull(this as? JsonString) {
    "JsonString expected but was ${this::class.simpleName}"
}.value

/**
 * Interprets given [Json] as [JsonNumber] and returns its value as [Number].
 * @throws IllegalArgumentException if JSON structure does not match expected type.
 */
fun Json.number() = requireNotNull(this as? JsonNumber) {
    "JsonNumber expected but was ${this::class.simpleName}"
}.value

/**
 * Interprets given [Json] as [JsonNumber] and returns its value as [Long].
 * @throws IllegalArgumentException if JSON structure does not match expected type.
 */
fun Json.long() = number().toLong()

/**
 * Interprets given [Json] as [JsonNumber] and returns its value as [Int].
 * @throws IllegalArgumentException if JSON structure does not match expected type.
 */
fun Json.int() = number().toInt()

/**
 * Interprets given [Json] as [JsonNumber] and returns its value as [Short].
 * @throws IllegalArgumentException if JSON structure does not match expected type.
 */
fun Json.short() = number().toShort()

/**
 * Interprets given [Json] as [JsonNumber] and returns its value as [Byte].
 * @throws IllegalArgumentException if JSON structure does not match expected type.
 */
fun Json.byte() = number().toByte()

/**
 * Interprets given [Json] as [JsonNumber] and returns its value as [Double].
 * @throws IllegalArgumentException if JSON structure does not match expected type.
 */
fun Json.double() = number().toDouble()

/**
 * Interprets given [Json] as [JsonNumber] and returns its value as [Float].
 * @throws IllegalArgumentException if JSON structure does not match expected type.
 */
fun Json.float() = number().toFloat()

/**
 * Interprets given [Json] as [JsonBoolean] and returns boolean value.
 * @throws IllegalArgumentException if JSON structure does not match expected type.
 */
fun Json.boolean() = requireNotNull(this as? JsonBoolean) {
    "JsonBoolean expected but was ${this::class.simpleName}"
}.value

/**
 * Returns null if provided [Json] is [JsonNull] or
 * interprets given [Json] as [JsonObject].
 * @throws IllegalArgumentException if JSON structure does not match expected type.
 */
fun Json.objOrNull() = if (this == JsonNull) null else obj()

/**
 * Returns null if provided [Json] is [JsonNull] or
 * interprets given [Json] as [JsonArray] and returns list of [Json] values.
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.arrayOrNull() = if (this == JsonNull) null else array()

/**
 * Returns null if provided [Json] is [JsonNull] or
 * interprets given [Json] as [JsonString] and returns int value.
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.stringOrNull() = if (this == JsonNull) null else string()

/**
 * Returns null if provided [Json] is [JsonNull] or
 * interprets given [Json] as [JsonNumber] and returns its value as [Number].
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.numberOrNull() = if (this == JsonNull) null else number()

/**
 * Returns null if provided [Json] is [JsonNull] or
 * interprets given [Json] as [JsonNumber] and returns its value as [Long].
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.longOrNull() = if (this == JsonNull) null else long()

/**
 * Returns null if provided [Json] is [JsonNull] or
 * interprets given [Json] as [JsonNumber] and returns its value as [Int].
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.intOrNull() = if (this == JsonNull) null else int()

/**
 * Returns null if provided [Json] is [JsonNull] or
 * interprets given [Json] as [JsonNumber] and returns its value as [Short].
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.shortOrNull() = if (this == JsonNull) null else short()

/**
 * Returns null if provided [Json] is [JsonNull] or
 * interprets given [Json] as [JsonNumber] and returns its value as [Byte].
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.byteOrNull() = if (this == JsonNull) null else byte()

/**
 * Returns null if provided [Json] is [JsonNull] or
 * interprets given [Json] as [JsonNumber] and returns its value as [Double].
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.doubleOrNull() = if (this == JsonNull) null else double()

/**
 * Returns null if provided [Json] is [JsonNull] or
 * interprets given [Json] as [JsonNumber] and returns its value as [Float].
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.floatOrNull() = if (this == JsonNull) null else float()

/**
 * Returns null if provided [Json] is [JsonNull] or
 * interprets given [Json] as [JsonBoolean] and returns boolean value.
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.booleanOrNull() = if (this == JsonNull) null else boolean()

/**
 * Interprets given [Json] as [JsonObject] and
 * returns value of given key interpreted as [JsonObject].
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.obj(key: String) = obj().require(key).obj()

/**
 * Interprets given [Json] as [JsonObject].
 * Interprets value of given key as [JsonArray].
 * Returns list of [Json] values of the array.
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.array(key: String) = obj().require(key).array()

/**
 * Interprets given [Json] as [JsonObject].
 * Interprets value of given key as [JsonString].
 * Returns string value.
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.string(key: String) = obj().require(key).string()

/**
 * Interprets given [Json] as [JsonObject].
 * Interprets value of given key as [JsonNumber].
 * Returns its value as [Number].
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.number(key: String) = obj().require(key).number()

/**
 * Interprets given [Json] as [JsonObject].
 * Interprets value of given key as [JsonNumber].
 * Returns its value as [Long].
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.long(key: String) = obj().require(key).long()

/**
 * Interprets given [Json] as [JsonObject].
 * Interprets value of given key as [JsonNumber].
 * Returns its value as [Int].
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.int(key: String) = obj().require(key).int()

/**
 * Interprets given [Json] as [JsonObject].
 * Interprets value of given key as [JsonNumber].
 * Returns its value as [Short].
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.short(key: String) = obj().require(key).short()

/**
 * Interprets given [Json] as [JsonObject].
 * Interprets value of given key as [JsonNumber].
 * Returns its value as [Byte].
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.byte(key: String) = obj().require(key).byte()

/**
 * Interprets given [Json] as [JsonObject].
 * Interprets value of given key as [JsonNumber].
 * Returns its value as [Double].
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.double(key: String) = obj().require(key).double()

/**
 * Interprets given [Json] as [JsonObject].
 * Interprets value of given key as [JsonNumber].
 * Returns its value as [Float].
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.float(key: String) = obj().require(key).float()

/**
 * Interprets given [Json] as [JsonObject].
 * Interprets value of given key as [JsonBoolean].
 * Returns boolean value.
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.boolean(key: String) = obj().require(key).boolean()

/**
 * Interprets given [Json] as [JsonObject] and
 * returns value of given key interpreted as [JsonObject]
 * or null if key is not exists or value is null.
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.objOrNull(key: String) = obj()[key]?.objOrNull()

/**
 * Interprets given [Json] as [JsonObject].
 * Returns null if given key is not exists or value of given key is null.
 * Otherwise, interprets value of given key as [JsonArray]
 * and returns list of [Json].
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.arrayOrNull(key: String) = obj()[key]?.arrayOrNull()

/**
 * Interprets given [Json] as [JsonObject].
 * Returns null if given key is not exists or value of given key is null.
 * Otherwise, interprets value of given key as [JsonString]
 * and returns string value of [JsonString].
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.stringOrNull(key: String) = obj()[key]?.stringOrNull()

/**
 * Interprets given [Json] as [JsonObject].
 * Returns null if given key is not exists or value of given key is null.
 * Otherwise, interprets value of given key as [JsonNumber]
 * and returns its value as [Number].
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.numberOrNull(key: String) = obj()[key]?.numberOrNull()

/**
 * Interprets given [Json] as [JsonObject].
 * Returns null if given key is not exists or value of given key is null.
 * Otherwise, interprets value of given key as [JsonNumber]
 * and returns its value as [Long].
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.longOrNull(key: String) = obj()[key]?.longOrNull()

/**
 * Interprets given [Json] as [JsonObject].
 * Returns null if given key is not exists or value of given key is null.
 * Otherwise, interprets value of given key as [JsonNumber]
 * and returns its value as [Int].
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.intOrNull(key: String) = obj()[key]?.intOrNull()

/**
 * Interprets given [Json] as [JsonObject].
 * Returns null if given key is not exists or value of given key is null.
 * Otherwise, interprets value of given key as [JsonNumber]
 * and returns its value as [Short].
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.shortOrNull(key: String) = obj()[key]?.shortOrNull()

/**
 * Interprets given [Json] as [JsonObject].
 * Returns null if given key is not exists or value of given key is null.
 * Otherwise, interprets value of given key as [JsonNumber]
 * and returns its value as [Byte].
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.byteOrNull(key: String) = obj()[key]?.byteOrNull()

/**
 * Interprets given [Json] as [JsonObject].
 * Returns null if given key is not exists or value of given key is null.
 * Otherwise, interprets value of given key as [JsonNumber]
 * and returns its value as [Double].
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.doubleOrNull(key: String) = obj()[key]?.doubleOrNull()

/**
 * Interprets given [Json] as [JsonObject].
 * Returns null if given key is not exists or value of given key is null.
 * Otherwise, interprets value of given key as [JsonNumber]
 * and returns its value as [Float].
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.floatOrNull(key: String) = obj()[key]?.floatOrNull()

/**
 * Interprets given [Json] as [JsonObject].
 * Returns null if given key is not exists or value of given key is null.
 * Otherwise, interprets value of given key as [JsonBoolean]
 * and returns boolean value of [JsonBoolean].
 * @throws IllegalArgumentException if JSON structure does not match expected type
 */
fun Json.booleanOrNull(key: String) = obj()[key]?.booleanOrNull()

private operator fun JsonObject.get(key: String) = value[key]

private fun JsonObject.require(key: String) = requireNotNull(value[key]) {
    "Value for key: $key is not provided"
}
