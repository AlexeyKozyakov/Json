import io.github.alexeykozyakov.json.accessors.array
import io.github.alexeykozyakov.json.accessors.boolean
import io.github.alexeykozyakov.json.accessors.float
import io.github.alexeykozyakov.json.accessors.int
import io.github.alexeykozyakov.json.accessors.string
import io.github.alexeykozyakov.json.accessors.stringOrNull
import io.github.alexeykozyakov.json.parser.parseJson

fun main() {
    val jsonString = """
        {
            "students": [
                {
                    "id": 1,
                    "first_name": "John",
                    "last_name": "Doe",
                    "age": 22,
                    "weight": 88.3,
                    "is_completed_studies": false,
                    "subjects": [
                        1,
                        3,
                        5
                    ]
                },
                {
                    "id": 2,
                    "first_name": "Alexey",
                    "last_name": "Kozyakov",
                    "age": 28,
                    "weight": 75.1,
                    "is_completed_studies": true,
                    "subjects": [
                        1,
                        2
                    ]
                },
                {
                    "id": 3,
                    "first_name": "Elena",
                    "last_name": "Ivanova",
                    "age": 18,
                    "weight": 60.7,
                    "is_completed_studies": false,
                    "subjects": [
                        3,
                        4,
                        5
                    ]
                }
            ],
            "subjects": [
                {
                    "id": 1,
                    "name": "Math",
                    "description": "Very useful subject"
                },
                {
                    "id": 2,
                    "name": "English",
                    "description": "Another very important subject"
                },
                {
                    "id": 3,
                    "name": "Computer Science",
                    "description": "Study how computers work"
                },
                {
                    "id": 4,
                    "name": "Economics",
                    "description": "There is the ability to study various financial systems."
                },
                {
                    "id": 5,
                    "name": "Philosophy"
                }
            ]
        }
    """.trimIndent()

    val json = parseJson(jsonString)

    val data = StudentsData(
        students = json.array("students").map { studentJson ->
            Student(
                id = studentJson.int("id"),
                firstName = studentJson.string("first_name"),
                lastName = studentJson.string("last_name"),
                age = studentJson.int("age"),
                weight = studentJson.float("weight"),
                isCompletedStudies = studentJson.boolean("is_completed_studies"),
                subjects = studentJson.array("subjects").map { subjectJson ->
                    subjectJson.int()
                }
            )
        },
        subjects = json.array("subjects").map { subjectJson ->
            Subject(
                id = subjectJson.int("id"),
                name = subjectJson.string("name"),
                description = subjectJson.stringOrNull("description")
            )
        }
    )

    println(data)
}
