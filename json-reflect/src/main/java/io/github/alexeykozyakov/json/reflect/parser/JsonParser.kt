package io.github.alexeykozyakov.json.reflect.parser

import io.github.alexeykozyakov.json.accessors.*
import io.github.alexeykozyakov.json.parser.JsonParsingException
import io.github.alexeykozyakov.json.parser.parseJson
import io.github.alexeykozyakov.json.reflect.*
import io.github.alexeykozyakov.json.representation.*
import kotlin.reflect.*
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.isSuperclassOf
import kotlin.reflect.full.primaryConstructor

/**
 * Parses JSON from [input] string as type [T].
 *
 * T can be one of the following:
 *
 *  - String
 *  - Number and its subclasses
 *  - Boolean
 *  - Enum class
 *  - Any
 *  - [Json] and its subclasses
 *  - T?
 *  - List<T>, Sequence<T>, Iterable<T>, Collection<T>, Map<String, T>
 *  - class with primary constructor with args of type T1, T2, ... Tn
 *
 * @throws JsonParsingException if parsing error was occurred
 */
inline fun <reified T> fromJson(input: String): T {
    val json = parseJson(input)
    return fromJson(json, typeOf<T>()) as T
}

/**
 * Maps given [Json] to type [T].
 *
 * T can be one of the following:
 *
 *  - String
 *  - Number and its subclasses
 *  - Boolean
 *  - Enum class
 *  - Any
 *  - [Json] and its subclasses
 *  - T?
 *  - List<T>, Sequence<T>, Iterable<T>, Collection<T>, Map<String, T>
 *  - class with primary constructor with args of type T1, T2, ... Tn
 *
 * @throws JsonParsingException if mapping error was occurred
 */
inline fun <reified T> fromJsonRepresentation(json: Json): T {
    return fromJson(json, typeOf<T>()) as T
}

/**
 * Internal function, use [fromJson] or [fromJsonRepresentation] instead.
 */
fun fromJson(json: Json, type: KType, key: String? = null): Any? {
    return try {
        if (json == JsonNull) {
            when {
                type.classifier == Json::class || type.classifier == JsonNull::class -> json
                type.isMarkedNullable -> null
                else -> error("Nonnull value expected")
            }
        } else {
            val kClass = checkNotNull(type.classifier as? KClass<*>) { "Class not provided" }
            when (kClass) {
                String::class -> json.string()

                Boolean::class -> json.boolean()

                Long::class -> json.long()

                Int::class -> json.int()

                Short::class -> json.short()

                Byte::class -> json.byte()

                Double::class -> json.double()

                Float::class -> json.float()

                Number::class -> json.number()

                Char::class -> error("Unsupported primitive type Char")

                Any::class -> anyFromJson(json)

                Map::class -> mapFromJson(json, type)

                Sequence::class -> listFromJson(json, type, key).asSequence()

                else -> {
                    when {
                        kClass.isSuperclassOf(List::class) -> listFromJson(json, type, key)

                        kClass.isSubclassOf(Enum::class) -> enumFromJson(json, kClass)

                        kClass.isSubclassOf(Json::class) -> checkNotNull(kClass.safeCast(json)) {
                            "Expected ${kClass.simpleName} but got ${json::class.simpleName}"
                        }

                        else -> objectFromJson(json, kClass)
                    }
                }
            }
        }
    } catch (e: JsonParsingException) {
        throw e
    } catch (e: Exception) {
        throw JsonParsingException(e.message + if (key != null) " for key: \"$key\"" else "", e)
    }
}

private fun anyFromJson(json: Json): Any? {
    return when (json) {
        JsonNull -> null
        is JsonString -> json.value
        is JsonNumber -> json.value
        is JsonBoolean -> json.value
        is JsonArray -> json.value.map(::anyFromJson)
        is JsonObject -> json.value.mapValues { (_, innerJson) -> anyFromJson(innerJson) }
    }
}

private fun mapFromJson(json: Json, type: KType): Map<String, Any?> {
    val keyType = checkNotNull(type.arguments.first().type) {
        "Star projection as map key is unsupported"
    }
    check(!keyType.isMarkedNullable) {
        "Non-nullable key type expected"
    }
    val keyClass = keyType.classifier as? KClass<*>
    check(keyClass == String::class) {
        "Expected map key type to be String, but was ${keyClass?.simpleName ?: "unknown"}"
    }
    val valueType = checkNotNull(type.arguments[1].type) {
        "Star projection as map value is unsupported"
    }
    return json.obj().value.mapValues { (innerKey, innerJson) ->
        fromJson(innerJson, valueType, innerKey)
    }
}

private fun listFromJson(json: Json, type: KType, key: String?): List<Any?> {
    val values = json.array()
    val valueType = type.arguments.first().type
    check(valueType != null) { "Got unsupported List<*>" }
    return values.map { innerJson ->
        fromJson(innerJson, valueType, key)
    }
}

private fun enumFromJson(json: Json, kClass: KClass<*>): Any {
    ensureMarkerInterfaceImplemented(kClass)
    val value = json.string()
    val enumConstantField = kClass.java.declaredFields.firstOrNull { field ->
        val nameAnnotation = field.getAnnotation(JsonName::class.java)
        val name = nameAnnotation?.name ?: field.name
        name == value
    } ?: error("Cannot find enum constant with name $value")
    enumConstantField.isAccessible = true
    return enumConstantField.get(null)
}

private fun objectFromJson(json: Json, kClass: KClass<*>): Any {
    ensureMarkerInterfaceImplemented(kClass)
    val mapper = kClass.getCompanionObject() as? JsonMapper<*>
    if (mapper != null) {
        val value = mapper::fromJson.allowAccessAndCall(json)
        check(value::class.isSubclassOf(kClass)) {
            "Incorrect type returned from mapper. " +
                    "Required subclass of ${kClass.simpleName}, but got ${value::class.simpleName}"
        }
        return value
    }
    val constructor = checkNotNull(kClass.primaryConstructor) {
        "Primary constructor or JsonMapper of class ${kClass.simpleName} not found"
    }
    val args = mutableMapOf<KParameter, Any?>()
    for (parameter in constructor.parameters) {
        val nameAnnotation = parameter.findAnnotation<JsonName>()
        val innerKey = nameAnnotation?.name
            ?: parameter.name
            ?: error("Constructor parameter name or JsonName annotation is required in class ${kClass.simpleName}")
        val innerJson = json.obj().value[innerKey]
        if (innerJson != null) {
            args[parameter] = fromJson(innerJson, parameter.type, key = innerKey)
        } else {
            when {
                parameter.isOptional -> Unit
                parameter.type.isMarkedNullable -> args[parameter] = null
                else -> error("Required value for inner key \"$innerKey\" is not provided")
            }
        }
    }
    return constructor.allowAccessAndCallBy(args)
}

private fun ensureMarkerInterfaceImplemented(kClass: KClass<*>) {
    if (!isAndroid) return
    check(kClass.isSubclassOf(JsonModel::class)) {
        """
            Trying to deserialize class ${kClass.simpleName}, which is not implementing JsonModel interface.
            You are likely using json-reflect library in android application,
            so to disable some R8 optimizations that may break deserialization, you need
            to implement JsonModel marker interface on your DTO classes.
        """.trimIndent()
    }
}
