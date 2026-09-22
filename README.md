# Json
Lightweight Json parser and serializer for Kotlin/JVM
- zero runtime dependencies
- Kotlin-first API
- parse JSON without reflection
- optional reflection-based serialization

[![Maven Central](https://img.shields.io/maven-central/v/io.github.alexeykozyakov.json/json.svg)](...)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.4.20%2B-purple)](...)
[![License](https://img.shields.io/github/license/alexeykozyakov/Json)](...)
[![Build](https://github.com/alexeykozyakov/Json/actions/workflows/test.yml/badge.svg)](...)
## Modules
Json parser consists of two independent modules:
### json
Low-level JSON API

Features:
- Parse JSON into Kotlin JSON representation based on sealed classes
- Convenient json field getters
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
```kotlin
data class User(
    val name: String,
    val age: Int
)

// Parse json
val json = parseJson("""{"name":"Alex","age":28}""")
val user = User(name = json.string("name"), age = json.int("age"))

// Build json and write it to string
val string = writeJson(
    jsonObj {
        string("name", "Alexey")
        int("age", 28)
    }
)
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
## Why Json parser?
- json parser focuses on simplicity and a Kotlin-friendly API
- unlike annotation-based libraries, you can work directly with JSON representation
- for projects that need automatic mapping, use json-reflect
