package io.github.alexeykozyakov.json.parser

import io.github.alexeykozyakov.json.accessors.array
import io.github.alexeykozyakov.json.accessors.boolean
import io.github.alexeykozyakov.json.accessors.int
import io.github.alexeykozyakov.json.builder.jsonObj
import io.github.alexeykozyakov.json.accessors.string
import io.github.alexeykozyakov.json.accessors.stringOrNull
import org.junit.Assert
import org.junit.Test

class JsonParserTest {
    @Test
    fun parseJsonSimple() {
        val json = """
        {
            "hello": "world",
            "int": 123,
            "float": 0.123,
            "null": null,
            "bool": true
        }
    """.trimIndent()

        val actual = parseJson(json)

        val expected = jsonObj {
            string("hello", "world")
            int("int", 123)
            float("float", 0.123)
            nul("null")
            boolean("bool", true)
        }

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun parseJsonComplex() {
        val json = """
        {
            "hello": "world",
            "int": 123,
            "float": 0.123,
            "null": null,
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
            "object": {
                "key": "value",
                "array": [1, 2, 3, 4]
            }
        }
    """.trimIndent()

        val actual = parseJson(json)

        val expected = jsonObj {
            string("hello", "world")
            int("int", 123)
            float("float", 0.123)
            nul("null")
            boolean("bool", true)
            array("array") {
                obj {
                    int("id", 1)
                    string("name", "first")
                }
                obj {
                    int("id", 2)
                    string("name", "second")
                }
                obj {
                    int("id", 3)
                    string("name", "third")
                }
            }
            obj("object") {
                string("key", "value")
                array("array") {
                    int(1)
                    int(2)
                    int(3)
                    int(4)
                }
            }
        }

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun parseJsonToDataClass() {
        data class Course(
            val id: Int,
            val name: String,
            val description: String
        )

        data class Student(
            val age: Int,
            val gender: Boolean,
            val name: String,
            val courses: List<Course>
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
                                "name": "Swimming",
                                "description": "How to swim"
                            }
                        ]
                    }
                ]
            }
        """.trimIndent()

        val parsed = parseJson(json)

        val actual = parsed.array("students").map { studentJson ->
            Student(
                age = studentJson.int("age"),
                gender = studentJson.boolean("gender"),
                name = studentJson.string("name"),
                courses = studentJson.array("courses").map { courseJson ->
                    Course(
                        id = courseJson.int("id"),
                        name = courseJson.string("name"),
                        description = courseJson.string("description")
                    )
                }
            )
        }

        val expected = listOf(
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
                        description = "How to swim"
                    )
                )
            )
        )

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun parseOptionalFields() {
        data class OptionalData(
            val hello: String?,
            val int: Int,
            val bool: Boolean,
            val name: String?
        )

        val json = """
        {
            "hello": null,
            "int": 123,
            "bool": true
        }
    """.trimIndent()

        val expected = OptionalData(
            hello = null,
            int = 123,
            bool = true,
            name = null
        )

        val parsed = parseJson(json)

        val actual = OptionalData(
            hello = parsed.stringOrNull("hello"),
            int = parsed.int("int"),
            bool = parsed.boolean("bool"),
            name = parsed.stringOrNull("name")
        )

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun parseJsonStringWithEscapedCharacters() {
        val json = """
        {
            "string": "123\n\t66\u0410\uD83D\uDE00\""
        }
    """.trimIndent()

        val actual = parseJson(json)

        val expected = jsonObj {
            string("string", "123\n\t66А😀\"")
        }

        Assert.assertEquals(expected, actual)
    }
}
