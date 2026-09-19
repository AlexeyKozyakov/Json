package com.alexey.kozyakov.com.alexey.kozyakov.json.writer

import com.alexey.kozyakov.json.representation.jsonObj
import com.alexey.kozyakov.json.writer.writeJson
import org.junit.Assert
import org.junit.Test

class JsonWriterTest {
    @Test
    fun writeJsonSimple() {
        val json = jsonObj {
            string("hello", "world")
            int("int", 123)
            float("float", 0.123)
            boolean("bool", true)
        }
        val expected = """
        {
            "hello": "world",
            "int": 123,
            "float": 0.123,
            "bool": true
        }
    """.trimIndent()

        val actual = writeJson(json)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun writeJsonComplex() {
        val json = jsonObj {
            string("hello", "world")
            int("int", 123)
            float("float", 0.123)
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
                    obj {
                        boolean("deep", true)
                    }
                }
            }
        }
        val expected = """
        {
            "hello": "world",
            "int": 123,
            "float": 0.123,
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
                "array": [
                    1,
                    2,
                    3,
                    4,
                    {
                        "deep": true
                    }
                ]
            }
        }
        """.trimIndent()

        val actual = writeJson(json)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun writeJsonFromDataClass() {
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

        val expected = """
            {
                "students": [
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

        val students = listOf(
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

        val json = jsonObj {
            array("students") {
                students.forEach { student ->
                    obj {
                        int("age", student.age)
                        boolean("gender", student.gender)
                        string("name", student.name)
                        array("courses") {
                            student.courses.forEach { course ->
                                obj {
                                    int("id", course.id)
                                    string("name", course.name)
                                    string("description", course.description)
                                }
                            }
                        }
                    }
                }
            }
        }

        val actual = writeJson(json)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun writeJsonWithoutPrettyPrint() {
        val json = jsonObj {
            string("hello", "world")
            int("int", 123)
            float("float", 0.123)
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
                    obj {
                        boolean("deep", true)
                    }
                }
            }
        }
        val expected =
            """{"hello":"world","int":123,"float":0.123,"bool":true,"array":[{"id":1,"name":"first"},{"id":2,"name":"second"},{"id":3,"name":"third"}],"object":{"key":"value","array":[1,2,3,4,{"deep":true}]}}"""

        val actual = writeJson(json, pretty = false)

        Assert.assertEquals(expected, actual)
    }
}
