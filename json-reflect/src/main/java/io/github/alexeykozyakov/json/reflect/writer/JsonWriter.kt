package io.github.alexeykozyakov.json.reflect.writer

import io.github.alexeykozyakov.json.reflect.*
import io.github.alexeykozyakov.json.representation.*
import io.github.alexeykozyakov.json.writer.writeJson
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.typeOf

/**
 * Writes value of subtype of [T] to JSON string.
 *
 * T can be one of the following:
 *
 *  - String
 *  - Number and its subclasses
 *  - Boolean
 *  - Enum
 *  - Any
 *  - T?
 *  - [Json] and its subclasses
 *  - class with custom JsonMapper
 *  - List<T>, Sequence<T>, Iterable<T>, Collection<T>, Map<String, T>
 *  - class with properties of type T1, T2, ... Tn
 *
 * @param omitNulls enables omitting of null properties during class serialization.
 *
 * @throws JsonWritingException if provided type cannot be written to JSON.
 */
inline fun <reified T> T.toJson(omitNulls: Boolean = true): String {
    val json = toJson(this, typeOf<T>(), omitNulls)
    return writeJson(json)
}

/**
 * Maps value of subtype of [T] to [Json].
 *
 * T can be one of the following:
 *
 *  - String
 *  - Number and its subclasses
 *  - Boolean
 *  - Enum
 *  - Any
 *  - T?
 *  - [Json] and its subclasses
 *  - class with custom JsonMapper
 *  - List<T>, Sequence<T>, Iterable<T>, Collection<T>, Map<String, T>
 *  - class with properties of type T1, T2, ... Tn
 *
 * @param omitNulls enables omitting of null properties during mapping.
 *
 * @throws JsonWritingException if provided type cannot be mapped to [Json].
 */
inline fun <reified T> T.toJsonRepresentation(omitNulls: Boolean = true): Json {
    return toJson(this, typeOf<T>(), omitNulls)
}

/**
 * Exception which can be thrown while JSON writing if
 * it contains unsupported fields.
 */
class JsonWritingException(message: String, cause: Exception? = null) :
    IllegalArgumentException(message, cause)

/**
 * Internal function, use [toJson] or [toJsonRepresentation] instead.
 */
fun toJson(value: Any?, type: KType?, omitNulls: Boolean, key: String? = null): Json {
    return when (value) {
        is String -> JsonString(value)

        is Number -> JsonNumber(value)

        is Boolean -> JsonBoolean(value)

        is Enum<*> -> enumToJson(value, key)

        null -> JsonNull

        is Char -> writingError("Unsupported primitive type Char", key)

        is Json -> value

        is Map<*, *> -> mapToJson(value, type, key, omitNulls)

        is Iterable<*> -> iterableToJson(value, type, key, omitNulls)

        is Sequence<*> -> iterableToJson(value.asIterable(), type, key, omitNulls)

        else -> objectToJson(value, type, key, omitNulls)
    }
}

private fun enumToJson(value: Enum<*>, key: String?): JsonString {
    val kClass = value::class
    ensureMarkerInterfaceImplemented(kClass, key)
    val field = kClass.java.getDeclaredField(value.name)
    val nameAnnotation = field.getAnnotation(JsonName::class.java)
    val name = nameAnnotation?.name ?: value.name
    return JsonString(name)
}

private fun mapToJson(value: Map<*, *>, type: KType?, key: String?, omitNulls: Boolean): JsonObject {
    val innerType = type?.arguments?.getOrNull(1)?.type
    val innerValues = mutableMapOf<String, Json>()
    value.forEach { (innerKey, innerValue) ->
        if (innerKey == null) writingError("Keys of map should be nonnull", key)
        if (innerKey !is String) writingError("Only String type of map keys is supported", key)
        innerValues[innerKey] = toJson(innerValue, innerType, omitNulls, innerKey)
    }
    return JsonObject(value = innerValues)
}

private fun iterableToJson(value: Iterable<*>, type: KType?, key: String?, omitNulls: Boolean): JsonArray {
    val innerType = type?.arguments?.firstOrNull()?.type
    val innerValues = value.map { item -> toJson(item, innerType, omitNulls, key) }
    return JsonArray(value = innerValues)
}

private fun objectToJson(value: Any, type: KType?, key: String?, omitNulls: Boolean): Json {
    val kClass = (type?.classifier as? KClass<*>)
        ?: writingError("Cannot get class of serializing object", key)
    ensureMarkerInterfaceImplemented(kClass, key)
    val mapper = kClass.getCompanionObject() as? JsonMapper<*>
    return if (mapper != null) {
        mapper::toJson.allowAccessAndCall(value)
    } else {
        val properties = value::class.getPropertiesInDeclarationOrderIfPossible()
        val values = mutableMapOf<String, Json>()
        for (property in properties) {
            val skipAnnotation = property.findAnnotation<JsonSkip>()
            if (skipAnnotation != null) continue
            val propertyValue = property.allowAccessAndCall(value)
            if (propertyValue == null && omitNulls) continue
            val nameAnnotation = property.findAnnotation<JsonName>()
            val innerKey = nameAnnotation?.name ?: property.name
            values[innerKey] = toJson(propertyValue, property.returnType, omitNulls, innerKey)
        }
        JsonObject(value = values)
    }
}

private fun writingError(message: String, key: String?): Nothing {
    throw JsonWritingException("$message ${if (key != null) " for key: $key" else ""}")
}

private fun ensureMarkerInterfaceImplemented(kClass: KClass<*>, key: String?) {
    if (!isAndroid) return
    if (!kClass.isSubclassOf(JsonModel::class)) {
        writingError(
            """
                Trying to serialize class ${kClass.simpleName}, which is not implementing JsonModel interface.
                You are likely using json-reflect library in android application,
                so to disable some R8 optimizations that may break serialization, you need
                to implement JsonModel marker interface on your DTO classes.
            """.trimIndent(),
            key
        )
    }
}
