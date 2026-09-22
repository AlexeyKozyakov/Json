package io.github.alexeykozyakov.json.reflect.writer

import org.junit.Assert
import org.junit.Test

class JsonWriterTest {
    @Test
    fun writeStringToJson() {
        val value = "Hello world!"
        val json = value.toJson()
        Assert.assertEquals("\"Hello world!\"", json)
    }

    @Test
    fun writeIntToJson() {
        val value = 69
        val json = value.toJson()
        Assert.assertEquals("69", json)
    }

    @Test
    fun writeFloatToJson() {
        val value = -0.4
        val json = value.toJson()
        Assert.assertEquals("-0.4", json)
    }

    @Test
    fun writeBooleanToJson() {
        val value = false
        val json = value.toJson()
        Assert.assertEquals("false", json)
    }

    @Test
    fun writeNullToJson() {
        val value = null
        val json = value.toJson()
        Assert.assertEquals("null", json)
    }

    @Test
    fun writeIntArrayToJson() {
        val value = listOf(1, 2, 3, 4, 5)
        val json = value.toJson()
        Assert.assertEquals(
            """
                [
                    1,
                    2,
                    3,
                    4,
                    5
                ]
            """.trimIndent(),
            json
        )
    }

    @Test
    fun writeJsonSimple() {
        data class HelloData(
            val hello: String,
            val int: Int,
            val float: Double,
            val nil: Any?,
            val bool: Boolean
        )

        val data = HelloData(
            hello = "world",
            int = 123,
            float = 0.123,
            nil = null,
            bool = true
        )

        val expected = """
        {
            "bool": true,
            "float": 0.123,
            "hello": "world",
            "int": 123
        }
    """.trimIndent()

        val actual = data.toJson()

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun writeJsonComplex() {
        val expected = """
        {
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
            "bool": true,
            "float": 0.123,
            "hello": "world",
            "int": 123,
            "obj": {
                "array": [
                    1,
                    2,
                    3,
                    4
                ],
                "key": "value"
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

        val data = Data(
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

        val actual = data.toJson()

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun writeJsonComplexTwo() {
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

        val expected = """
        {
            "students": [
                {
                    "age": 18,
                    "courses": [
                        {
                            "description": "Very important course",
                            "id": 1,
                            "name": "Math"
                        },
                        {
                            "description": "Additional course",
                            "id": 2,
                            "name": "English"
                        }
                    ],
                    "gender": false,
                    "name": "Alex Kozyakov"
                },
                {
                    "age": 22,
                    "courses": [
                        {
                            "description": "Learning russian language",
                            "id": 4,
                            "name": "Russian"
                        },
                        {
                            "description": "Stay fit",
                            "id": 5,
                            "name": "Sport"
                        }
                    ],
                    "gender": true,
                    "name": "Elena Stepanova"
                },
                {
                    "age": 27,
                    "courses": [
                        {
                            "description": "Some additional course",
                            "id": 7,
                            "name": "Some course"
                        },
                        {
                            "id": 8,
                            "name": "Swimming"
                        }
                    ],
                    "gender": false,
                    "name": "Andrey Martinov"
                }
            ]
        }
        """.trimIndent()

        val data = Students(
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

        val actual = data.toJson()

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun writeNullsToJson() {
        data class Data(
            val nullable: String?,
            val nonnull: Int
        )

        val expected = """
            {
                "nonnull": 213,
                "nullable": null
            }
        """.trimIndent()

        val data = Data(
            nullable = null,
            nonnull = 213
        )

        val actual = data.toJson(omitNulls = false)

        Assert.assertEquals(expected, actual)
    }
}
