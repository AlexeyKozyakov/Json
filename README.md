# JsonParser
Lightweight JSON parser and serializer for Kotlin/JVM
- no runtime dependencies beyond the Kotlin standard library
- Kotlin-first API
- parse JSON without reflection
- optional reflection-based mapping between JSON and Kotlin classes

[![Maven Central](https://img.shields.io/maven-central/v/io.github.alexeykozyakov.json/json.svg)](https://central.sonatype.com/namespace/io.github.alexeykozyakov.json)
[![GitHub release](https://img.shields.io/github/v/release/AlexeyKozyakov/Json)](https://github.com/AlexeyKozyakov/Json/releases)
[![Build](https://github.com/alexeykozyakov/Json/actions/workflows/test.yml/badge.svg)](https://github.com/AlexeyKozyakov/Json/actions/workflows/test.yml)
[![License](https://img.shields.io/github/license/AlexeyKozyakov/Json)](https://github.com/AlexeyKozyakov/Json/blob/main/LICENSE)
## Modules
JsonParser is split into two independent artifacts:
### json
Low-level JSON API

Features:
- parse JSON into a Kotlin representation based on sealed classes
- convenient JSON field getters
- represents integer and floating-point numbers using Kotlin Number
- JSON builder DSL
- serialize JSON representation back to string
### json-reflect
Reflection-based mapping between JSON and Kotlin classes built on top of json

Features:
- maps JSON to Kotlin classes using their primary constructors
- serializes all class properties declared in code
- supports iterables, collections, lists and sequences
- supports custom JsonMapper implementations for complex types
## Installation
### json
[![Maven Central](https://img.shields.io/maven-central/v/io.github.alexeykozyakov.json/json.svg)](https://central.sonatype.com/artifact/io.github.alexeykozyakov.json/json)
```kotlin
dependencies {
    implementation("io.github.alexeykozyakov.json:json:1.0.6")
}
```
### json-reflect
[![Maven Central](https://img.shields.io/maven-central/v/io.github.alexeykozyakov.json/json-reflect.svg)](https://central.sonatype.com/artifact/io.github.alexeykozyakov.json/json-reflect)
```kotlin
dependencies {
    implementation("io.github.alexeykozyakov.json:json-reflect:1.0.6")
}
```
## Quick start
```kotlin
import io.github.alexeykozyakov.json.parser.parseJson

val json = parseJson("""{"name":"Alexey","age":28}""")
println(json.string("name"))
// Alexey

val output = writeJson(json)
println(output)
// {"name":"Alexey","age":28}
```
## json module
### JSON representation
The `json` module does not require DTO classes. 
You can build JSON structures directly:

```kotlin
val json = jsonObj {
    string("name", "Alexey")
    array("scores") {
        int(100)
        int(200)
    }
}

println(writeJson(json))
// {"name":"Alexey","scores":[100,200]}
```

### JSON parsing
```kotlin
data class User(
    val name: String,
    val age: Int
)

// Parse json
val json = parseJson("""{"name":"Alex","age":28}""")
val user = User(name = json.string("name"), age = json.int("age"))
```
## json-reflect module
### JSON to class mapping
```kotlin
data class User(
    val name: String,
    val age: Int
)

// Parse json
val user = fromJson<User>("""{"name": "Alex", "age": 28 }""")

// Write json to string
val json = user.toJson()
```
### Custom JSON mappers
In some cases mapping based on primary constructors and class properties is not enough.

Json-reflect module supports custom mapping logic which can be provided
by implementing JsonMapper interface on class companion object.
```kotlin
sealed interface Shape {
    data class Rectangle(val width: Int, val height: Int) : Shape
    data class Circle(val radius: Int) : Shape

    companion object : JsonMapper<Shape> {
        override fun toJson(value: Shape): Json {
            return when (value) {
                is Rectangle -> jsonObj {
                    string("type", "rectangle")
                    fields(value.toJsonRepresentation().obj())
                }
                is Circle -> jsonObj {
                    string("type", "circle")
                    fields(value.toJsonRepresentation().obj())
                }
            }
        }

        override fun fromJson(json: Json): Shape {
            val type = json.string("type")
            return when (type) {
                "rectangle" -> fromJsonRepresentation<Rectangle>(json)
                "circle" -> fromJsonRepresentation<Circle>(json)
                else -> error("Unknown shape")
            }
        }
    }
}

val shapes = fromJson<List<Shape>>(
    """
        [
            {
                "type": "rectangle",
                "width": 10,
                "height": 20
            },
            {
                "type": "circle",
                "radius": 5
            }
        ]
    """.trimIndent()
)

println(shapes)
// [Rectangle(width=10, height=20), Circle(radius=5)]
```

## Design goals
JsonParser focuses on a simple and Kotlin-friendly API.

The `json` module exposes JSON as a Kotlin representation that can be
inspected and manipulated directly:

```kotlin
val json = parseJson(input)
val name = json.string("name")
```

For automatic mapping between JSON and Kotlin classes, use json-reflect.
## License
Apache License 2.0
## Documentation
API documentation:
[https://alexeykozyakov.github.io/Json/](https://alexeykozyakov.github.io/Json/)
