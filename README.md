# Simple and convinient Json parser and serializer for Kotlin
- no runtime dependencies
- utilizes the conciseness of Kotlin syntax
- simple parsing and serealizing code
- optional reflection support
## Contains two separate artifacts
### json
- allows to parse json to kotlin representation based on sealed classes
- provides convenient getters of json fields
- provides simple json building dsl
- allows to serialize constructed json representation to string
### json-reflect
- built on top of json module
- includes all capabilities from json module
- in addition, provides methods to serealize and deserialize kotlin classes
- this methods use reflection under the hood
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
## Usage of :json module
### Parsing json
```kotlin
data class Data(
    val hello: String,
    val int: Int,
    val float: Double,
    val nul: String?,
    val bool: Boolean,
    val array: List<Int>
)

val input = """
{
    "hello": "world",
    "int": 123,
    "float": 0.123,
    "nul": null,
    "bool": true,
    "array": [1, 2, 3]
}
"""

val json = parseJson(input)

val data = Data(
    hello = json.string("hello"),
    int = json.int("int"),
    float = json.float("float"),
    nul = json.stringOrNull("nul"),
    bool = json.boolean("bool"),
    array = json.array("array").map { it.int() }
)
```
### Writing json
```kotlin
val json = jsonObj {
    string("hello", "world")
    int("int", 123)
    float("float", 0.123)
    boolean("bool", true)
    obj("someObject") {
        array("array) {
            int(1),
            int(2)
            int(3)
        }
    }
}

writeJson(json)
```
## Usage of :json-reflect module
### Parsing json
```kotlin
val input = """
{
    "hello": "world",
    "int": 123,
    "float": 0.123,
    "nil": null,
    "bool": true
}
"""

data class HelloData(
    val hello: String,
    val int: Int,
    val float: Double,
    val nil: Any?,
    val bool: Boolean
)

val data = fromJson<HelloData>(input)
```
### Writing json
```kotlin
data class HelloData(
    val hello: String,
    val int: Int,
    val float: Double,
    val ints: List<Int>
)

val data = HelloData(
    hello = "world",
    int = 123,
    float = 0.123,
    ints = listOf(1, 2, 3, 4)
)

val json = data.toJson()
```
