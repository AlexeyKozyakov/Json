# JsonParser
Lightweight JSON parser and serializer for Kotlin/JVM
- no runtime dependencies beyond the Kotlin standard library
- Kotlin-first API
- parse JSON without reflection
- optional reflection-based JSON <-> class mapping

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
- generalizes working with integer and floating point numbers by using Kotlin Number type under the hood
- JSON builder DSL
- serialize JSON representation back to string
### json-reflect
Reflection-based JSON <-> class mapping built on top of json

Features
- reflection-based mapping: class <-> JSON string
- converts JSON to classes using primary constructor
- serializes all class properties declared in code
- supports iterables, collections, lists and sequences
- supports custom serialization and deserialization logic
## Installation
### json
[![Maven Central](https://img.shields.io/maven-central/v/io.github.alexeykozyakov.json/json.svg)](https://central.sonatype.com/artifact/io.github.alexeykozyakov.json/json)
```kotlin
dependencies {
    implementation("io.github.alexeykozyakov.json:json:x.x.x")
}
```
### json-reflect
[![Maven Central](https://img.shields.io/maven-central/v/io.github.alexeykozyakov.json/json-reflect.svg)](https://central.sonatype.com/artifact/io.github.alexeykozyakov.json/json-reflect)
```kotlin
dependencies {
    implementation("io.github.alexeykozyakov.json:json-reflect:x.x.x")
}
```
## Quick start
```kotlin
import io.github.alexeykozyakov.json.parser.parseJson

val json = parseJson("""{"name":"Alexey","age":28}""")

println(json.string("name"))
// Alexey
```
## json module
### JSON representation
The `json` module does not require DTO classes. 
You can manipulate JSON structures directly:

```kotlin
val json = jsonObj {
    string("name", "Alexey")
    array("scores") {
        int(100)
        int(200)
    }
}

println(writeJson(json))
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
        override fun toJson(value: Shape) = error("Not implemented")

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
Unlike annotation-based libraries, it allows direct work with JSON representation:

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
