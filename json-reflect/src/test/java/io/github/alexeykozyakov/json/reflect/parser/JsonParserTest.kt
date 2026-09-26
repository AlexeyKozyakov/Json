package io.github.alexeykozyakov.json.reflect.parser

import io.github.alexeykozyakov.json.accessors.array
import io.github.alexeykozyakov.json.accessors.float
import io.github.alexeykozyakov.json.accessors.int
import io.github.alexeykozyakov.json.accessors.string
import io.github.alexeykozyakov.json.builder.jsonObj
import io.github.alexeykozyakov.json.parser.JsonParsingException
import io.github.alexeykozyakov.json.reflect.JsonMapper
import io.github.alexeykozyakov.json.representation.Json
import org.junit.Assert
import org.junit.Test

class JsonParserTest {
    @Test
    fun parseString() {
        val json = "\"Hello world!\""
        val value = fromJson<String>(json)
        Assert.assertEquals("Hello world!", value)
    }

    @Test
    fun parseNull() {
        val json = "null"
        val value = fromJson<String?>(json)
        Assert.assertEquals(null, value)
    }

    @Test
    fun parseInt() {
        val json = "69"
        val value = fromJson<Int>(json)
        Assert.assertEquals(69, value)
    }

    @Test
    fun parseFloat() {
        val json = "-0.36"
        val value = fromJson<Double>(json)
        Assert.assertEquals(-0.36, value, 0.0)
    }

    @Test
    fun parseBoolean() {
        val json = "false"
        val value = fromJson<Boolean>(json)
        Assert.assertEquals(false, value)
    }

    @Test
    fun parseJsonSimple() {
        val json = """
        {
            "hello": "world",
            "int": 123,
            "float": 0.123,
            "nil": null,
            "bool": true
        }
    """.trimIndent()

        data class HelloData(
            val hello: String,
            val int: Int,
            val float: Double,
            val nil: Any?,
            val bool: Boolean
        )

        val expected = HelloData(
            hello = "world",
            int = 123,
            float = 0.123,
            nil = null,
            bool = true
        )

        val actual = fromJson<HelloData>(json)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun parseJsonComplex() {
        val json = """
        {
            "hello": "world",
            "int": 123,
            "float": 0.123,
            "nil": null,
            "bool": true,
            "array": [
                {
                    "id": 1,
                    "name": "first"
                },
                {
                    "id": 2,
                    "name": "second"
                },
                {
                    "id": 3,
                    "name": "third"
                }
            ],
            "obj": {
                "key": "value",
                "array": [1, 2, 3, 4]
            }
        }
    """.trimIndent()

        data class Obj(
            val id: Int,
            val name: String
        )

        data class AnotherObj(
            val key: String,
            val array: List<Int>
        )

        data class Data(
            val hello: String,
            val int: Int,
            val float: Double,
            val nil: Any?,
            val bool: Boolean,
            val array: List<Obj>,
            val obj: AnotherObj
        )

        val expected = Data(
            hello = "world",
            int = 123,
            float = 0.123,
            nil = null,
            bool = true,
            array = listOf(
                Obj(
                    id = 1,
                    name = "first"
                ),
                Obj(
                    id = 2,
                    name = "second"
                ),
                Obj(
                    id = 3,
                    name = "third"
                )
            ),
            obj = AnotherObj(
                key = "value",
                array = listOf(1, 2, 3, 4)
            )
        )

        val actual = fromJson<Data>(json)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun parseJsonTypeError() {
        val json = """
        {
            "hello": 600,
            "int": 123,
            "float": 0.123,
        }
    """.trimIndent()

        data class Data(
            val hello: String,
            val int: Int,
            val float: Double
        )

        val exception = Assert.assertThrows(JsonParsingException::class.java) {
            fromJson<Data>(json)
        }
        Assert.assertEquals(
            "JsonString expected but was JsonNumber for key: \"hello\"",
            exception.message
        )
    }

    @Test
    fun parseJsonArrayToSequence() {
        val json = """
        {
            "int": 123,
            "floats": [ 123.1, 234.2, 12312.2 ]
        }
    """.trimIndent()

        data class Data(
            val int: Int,
            val floats: Sequence<Double>
        ) {
            override fun equals(other: Any?): Boolean {
                if (other !is Data) return false
                return int == other.int && floats.toList() == other.floats.toList()
            }

            override fun hashCode(): Int {
                var result = int
                result = 31 * result + floats.toList().hashCode()
                return result
            }
        }

        val expected = Data(
            int = 123,
            floats = sequenceOf(123.1, 234.2, 12312.2)
        )

        val actual = fromJson<Data>(json)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun parseJsonArrayToIterable() {
        val json = """
        {
            "int": 123,
            "floats": [ 123.1, 234.2, 12312.2 ]
        }
    """.trimIndent()

        data class Data(
            val int: Int,
            val floats: Iterable<Double>
        )

        val expected = Data(
            int = 123,
            floats = listOf(123.1, 234.2, 12312.2)
        )

        val actual = fromJson<Data>(json)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun parseNestedObjectInArrayTypeError() {
        val json = """
        {
            "int": 123,
            "floats": [ 123.1, 234.2, { "value": 123.4 } ],
        }
    """.trimIndent()

        data class Data(
            val int: Int,
            val floats: List<Double>
        )

        val exception = Assert.assertThrows(JsonParsingException::class.java) {
            fromJson<Data>(json)
        }
        Assert.assertEquals(
            "JsonNumber expected but was JsonObject for key: \"floats\"",
            exception.message
        )
    }

    @Test
    fun parseNestedArrayInArrayTypeError() {
        val json = """
        {
            "int": 123,
            "floats": [ 123.1, 234.2, [ 123.4 ] ],
        }
    """.trimIndent()

        data class Data(
            val int: Int,
            val floats: List<Double>
        )

        val exception = Assert.assertThrows(JsonParsingException::class.java) {
            fromJson<Data>(json)
        }
        Assert.assertEquals(
            "JsonNumber expected but was JsonArray for key: \"floats\"",
            exception.message
        )
    }

    @Test
    fun parseNestedObjectInObjectTypeError() {
        val json = """
        {
            "int": 123,
            "floats": [ 123.0 ],
            "nested": {
                "id": 123,
                "name": { "id": 123 }
             }
        }
    """.trimIndent()

        data class Nested(
            val id: Int,
            val name: String
        )

        data class Data(
            val int: Int,
            val floats: List<Double>,
            val nested: Nested
        )

        val exception = Assert.assertThrows(JsonParsingException::class.java) {
            fromJson<Data>(json)
        }
        Assert.assertEquals(
            "JsonString expected but was JsonObject for key: \"name\"",
            exception.message
        )
    }

    @Test
    fun parseJsonUnsupportedPrimitiveTypeError() {
        val json = """
        {
            "int": 123,
            "char": "c"
        }
    """.trimIndent()

        data class Data(
            val int: Int,
            val char: Char
        )

        val exception = Assert.assertThrows(JsonParsingException::class.java) {
            fromJson<Data>(json)
        }
        Assert.assertEquals(
            "Unsupported primitive type Char for key: \"char\"",
            exception.message
        )
    }

    @Test
    fun parseJsonNoValueError() {
        val json = """
        {
            "int": 123,
            "float": 0.123,
        }
    """.trimIndent()

        data class Data(
            val hello: String,
            val int: Int,
            val float: Double
        )

        val exception = Assert.assertThrows(JsonParsingException::class.java) {
            fromJson<Data>(json)
        }
        Assert.assertEquals(
            "Required value for inner key \"hello\" is not provided",
            exception.message
        )
    }

    @Test
    fun parseJsonNoValueNestedError() {
        val json = """
        {
            "int": 123,
            "float": 0.123,
            "nested": {
                "int": 1
            }
        }
    """.trimIndent()

        data class Nested(
            val hello: String,
            val int: Int
        )

        data class Data(
            val int: Int,
            val float: Double,
            val nested: Nested
        )

        val exception = Assert.assertThrows(JsonParsingException::class.java) {
            fromJson<Data>(json)
        }
        Assert.assertEquals(
            "Required value for inner key \"hello\" is not provided for key: \"nested\"",
            exception.message
        )
    }

    @Test
    fun parseJsonNullValueInArrayError() {
        val json = """
        {
            "int": 123,
            "float": 0.123,
            "ints": [1, 2, 3, null]
        }
    """.trimIndent()

        data class Data(
            val int: Int,
            val float: Double,
            val ints: List<Int>
        )

        val exception = Assert.assertThrows(JsonParsingException::class.java) {
            fromJson<Data>(json)
        }
        Assert.assertEquals("Nonnull value expected for key: \"ints\"", exception.message)
    }

    @Test
    fun parseWildcardJsonArrayError() {
        val json = """
        {
            "int": 123,
            "float": 0.123,
            "ints": [1, null, 3, null]
        }
    """.trimIndent()

        data class Data(
            val int: Int,
            val float: Double,
            val ints: List<*>
        )

        val exception = Assert.assertThrows(JsonParsingException::class.java) {
            fromJson<Data>(json)
        }

        Assert.assertEquals("Got unsupported List<*> for key: \"ints\"", exception.message)
    }

    @Test
    fun parseJsonArrayWithNullValues() {
        val json = """
        {
            "int": 123,
            "float": 0.123,
            "ints": [1, null, 3, null]
        }
    """.trimIndent()

        data class Data(
            val int: Int,
            val float: Double,
            val ints: List<Int?>
        )

        val expected = Data(
            int = 123,
            float = 0.123,
            ints = listOf(1, null, 3, null)
        )

        val actual = fromJson<Data>(json)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun parseJsonComplexTwo() {
        data class Course(
            val id: Int,
            val name: String,
            val description: String?
        )

        data class Student(
            val age: Int,
            val gender: Boolean,
            val name: String,
            val courses: List<Course>
        )

        data class Students(
            val students: List<Student>
        )

        val json = """
            {
                "students" : [
                    {
                        "age": 18,
                        "gender": false,
                        "name": "Alex Kozyakov",
                        "courses": [
                            {
                                "id": 1,
                                "name": "Math",
                                "description": "Very important course"
                            },
                            {
                                "id": 2,
                                "name": "English",
                                "description": "Additional course"
                            }
                        ]
                    },
                    {
                        "age": 22,
                        "gender": true,
                        "name": "Elena Stepanova",
                        "courses": [
                            {
                                "id": 4,
                                "name": "Russian",
                                "description": "Learning russian language"
                            },
                            {
                                "id": 5,
                                "name": "Sport",
                                "description": "Stay fit"
                            }
                        ]
                    },
                    {
                        "age": 27,
                        "gender": false,
                        "name": "Andrey Martinov",
                        "courses": [
                            {
                                "id": 7,
                                "name": "Some course",
                                "description": "Some additional course"
                            },
                            {
                                "id": 8,
                                "name": "Swimming"
                            }
                        ]
                    }
                ]
            }
        """.trimIndent()


        val expected = Students(
            students = listOf(
                Student(
                    age = 18,
                    gender = false,
                    name = "Alex Kozyakov",
                    courses = listOf(
                        Course(
                            id = 1,
                            name = "Math",
                            description = "Very important course"
                        ),
                        Course(
                            id = 2,
                            name = "English",
                            description = "Additional course"
                        )
                    )
                ),
                Student(
                    age = 22,
                    gender = true,
                    name = "Elena Stepanova",
                    courses = listOf(
                        Course(
                            id = 4,
                            name = "Russian",
                            description = "Learning russian language"
                        ),
                        Course(
                            id = 5,
                            name = "Sport",
                            description = "Stay fit"
                        )
                    )
                ),
                Student(
                    age = 27,
                    gender = false,
                    name = "Andrey Martinov",
                    courses = listOf(
                        Course(
                            id = 7,
                            name = "Some course",
                            description = "Some additional course"
                        ),
                        Course(
                            id = 8,
                            name = "Swimming",
                            description = null
                        )
                    )
                )
            )
        )

        val actual = fromJson<Students>(json)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun parseNestedLists() {
        data class Data(
            val id: Int,
            val name: String
        )

        val json = """
            [
                [
                    {
                        "id": 1,
                        "name": "first"
                    },
                    {
                        "id": 2,
                        "name": "second"
                    }
                ],
                [
                    {
                        "id": 3,
                        "name": "third"
                    },
                    {
                        "id": 4,
                        "name": "fourth"
                    }
                ]
            ]
        """.trimIndent()

        val expected = listOf(
            listOf(
                Data(
                    id = 1,
                    name = "first"
                ),
                Data(
                    id = 2,
                    name = "second"
                )
            ),
            listOf(
                Data(
                    id = 3,
                    name = "third"
                ),
                Data(
                    id = 4,
                    name = "fourth"
                )
            )
        )

        val actual = fromJson<List<List<Data>>>(json)

        Assert.assertEquals(expected, actual)
    }

    private enum class Values {
        One,
        Two,
        Three
    }

    @Test
    fun parseEnum() {
        data class Data(val value: Values)

        val json = """
            {
                "value": "Two"
            }
        """.trimIndent()

        val expected = Data(value = Values.Two)

        val actual = fromJson<Data>(json)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun parseUndefinedEnumValue() {
        data class Data(val value: Values)

        val json = """
            {
                "value": "Tw"
            }
        """.trimIndent()

        val exception = Assert.assertThrows(JsonParsingException::class.java) {
            fromJson<Data>(json)
        }

        Assert.assertEquals("Undefined enum constant Tw for key: \"value\"", exception.message)
    }

    private sealed interface Data {
        data object Empty : Data
        data class IntAndFloat(val int: Int, val float: Float) : Data
        data class StringAndArray(val str: String, val array: List<Int>) : Data

        companion object : JsonMapper<Data> {
            override fun toJson(value: Data): Json = error("Not implemented")

            override fun fromJson(json: Json): Data {
                return when (val type = json.string("type")) {
                    "empty" -> Empty

                    "intAndFloat" -> IntAndFloat(
                        int = json.int("int"),
                        float = json.float("float")
                    )

                    "stringAndArray" -> StringAndArray(
                        str = json.string("str"),
                        array = json.array("array").map { it.int() }
                    )

                    else -> error("Unknown Data type: $type")
                }
            }

        }
    }

    @Test
    fun parseSealedClassesUsingCustomMapper() {
        val json = """
            [
                {
                    "type": "empty"
                },
                {
                    "type": "intAndFloat",
                    "int": 123,
                    "float": 66.3
                },
                {
                    "type": "stringAndArray",
                    "str": "Hello world!",
                    "array": [5, 3, 2]
                }
            ]
        """.trimIndent()

        val expected = listOf(
            Data.Empty,
            Data.IntAndFloat(int = 123, float = 66.3f),
            Data.StringAndArray(str = "Hello world!", array = listOf(5, 3, 2))
        )

        val actual = fromJson<List<Data>>(json)

        Assert.assertEquals(expected, actual)
    }

    private class Another
    private class Custom {
        companion object : JsonMapper<Another> {
            override fun toJson(value: Another) = error("Not implemented")
            override fun fromJson(json: Json) = Another()
        }
    }

    @Test
    fun parseClassesUsingCustomMapperErrorIncorrectMappedType() {
        val json = """{}"""

        val exception = Assert.assertThrows(JsonParsingException::class.java) {
            fromJson<Custom>(json)
        }

        Assert.assertEquals(
            "Incorrect type returned from mapper. Required subclass of Custom, but got Another",
            exception.message
        )
    }

    private sealed interface Shape {
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

    @Test
    fun parseSealedClassesExample() {
        val json = """
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

        val expected = listOf(
            Shape.Rectangle(width = 10, height = 20),
            Shape.Circle(radius = 5)
        )

        val actual = fromJson<List<Shape>>(json)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun paseJsonToMap() {
        val json = """
            {
                "name": "Ivan",
                "age": 23,
                "additionalData": {
                    "weight": 75.6,
                    "gender": "male",
                    "birthdate": "1990-01-01",
                    "subjects": [
                        "math",
                        "English",
                        "history"
                    ]
                }
            }
        """.trimIndent()

        data class Profile(
            val name: String,
            val age: Int,
            val additionalData: Map<String, Any>
        )

        val actual = fromJson<Profile>(json)

        val expected = Profile(
            name = "Ivan",
            age = 23,
            additionalData = mapOf(
                "weight" to 75.6,
                "gender" to "male",
                "birthdate" to "1990-01-01",
                "subjects" to listOf(
                    "math",
                    "English",
                    "history"
                )
            )
        )

        Assert.assertEquals(expected, actual)
    }
}
