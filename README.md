# Lightweight Json parser and serializer for Kotlin
- no runtime dependencies
- utilizes the conciseness of Kotlin syntax
- simple parsing and serializing code
- optional reflection support
## Modules
JsonParser consists of two independent modules:
### json
- allows to parse json to kotlin representation based on sealed classes
- provides convenient getters of json fields
- provides simple json building dsl
- allows to serialize constructed json representation to string
### json-reflect
- built on top of json module
- includes all capabilities from json module
- in addition, provides methods to serialize and deserialize kotlin classes
- these methods use reflection under the hood
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
writeJson(
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
