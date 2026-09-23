package io.github.alexeykozyakov.json.reflect.parser

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

        val exception = Assert.assertThrows(IllegalStateException::class.java) {
            fromJson<Data>(json)
        }
        Assert.assertEquals("JsonString expected but was JsonIntNumber for key: \"hello\"", exception.message)
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

        val exception = Assert.assertThrows(IllegalStateException::class.java) {
            fromJson<Data>(json)
        }
        Assert.assertEquals("JsonFloatNumber expected but was JsonObject for key: \"floats\"", exception.message)
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

        val exception = Assert.assertThrows(IllegalStateException::class.java) {
            fromJson<Data>(json)
        }
        Assert.assertEquals("JsonFloatNumber expected but was JsonArray for key: \"floats\"", exception.message)
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

        val exception = Assert.assertThrows(IllegalStateException::class.java) {
            fromJson<Data>(json)
        }
        Assert.assertEquals("JsonString expected but was JsonObject for key: \"name\"", exception.message)
    }

    @Test
    fun parseJsonUnsupportedPrimitiveTypeError() {
        val json = """
        {
            "int": 123,
            "float": 0.123,
        }
    """.trimIndent()

        data class Data(
            val int: Int,
            val float: Float
        )

        val exception = Assert.assertThrows(IllegalStateException::class.java) {
            fromJson<Data>(json)
        }
        Assert.assertEquals(
            "Unsupported primitive type: Float for key: \"float\"",
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

        val exception = Assert.assertThrows(IllegalStateException::class.java) {
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

        val exception = Assert.assertThrows(IllegalStateException::class.java) {
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

        val exception = Assert.assertThrows(IllegalStateException::class.java) {
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

        val exception = Assert.assertThrows(IllegalStateException::class.java) {
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
}
