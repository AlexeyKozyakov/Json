# Json
Lightweight Json parser and serializer for Kotlin/JVM
- zero runtime dependencies (except Kotlin standard library)
- Kotlin-first API
- parse JSON without reflection
- optional reflection-based serialization

[![Maven Central](https://img.shields.io/maven-central/v/io.github.alexeykozyakov.json/json.svg)](...)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.x%2B-purple)](...)
[![License](https://img.shields.io/github/license/alexeykozyakov/Json)](...)
[![Build](https://github.com/alexeykozyakov/Json/actions/workflows/test.yml/badge.svg)](...)
## Modules
JsonParser is split into two independent artifacts:
### json
Low-level JSON API

Features:
- Parse JSON into a Kotlin representation based on sealed classes
- Convenient JSON field getters
- JSON builder DSL
- Serialize JSON representation back to string
### json-reflect
Reflection-based mapper built on top of json

Features:
- Deserialize Kotlin classes from JSON
- Serialize Kotlin classes to JSON
- Supports data classes
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
val json = parseJson("""{"name":"Alexey","age":28}""")

println(json.string("name"))
// Alexey
```
## Usage of :json module
### JSON representation
The `json` module does not require Kotlin classes. 
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
## Usage of :json-reflect module
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
## Why Json?
JsonParser focuses on a simple and Kotlin-friendly API.
Unlike annotation-based libraries, it allows direct work with JSON representation:

```kotlin
val json = parseJson(input)

val name = json.string("name")
```
For automatic mapping between JSON and Kotlin classes, use json-reflect.
## License
Apache License 2.0
