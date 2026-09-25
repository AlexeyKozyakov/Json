import io.github.alexeykozyakov.json.builder.jsonObj
import io.github.alexeykozyakov.json.writer.writeJson

fun main() {
    val data = StudentsData(
        students = listOf(
            Student(
                id = 1,
                firstName = "John",
                lastName = "Doe",
                age = 22,
                weight = 88.3f,
                isCompletedStudies = false,
                subjects = listOf(1, 3, 5)
            ),
            Student(
                id = 2,
                firstName = "Alexey",
                lastName = "Kozyakov",
                age = 28,
                weight = 75.1f,
                isCompletedStudies = true,
                subjects = listOf(1, 2)
            ),
            Student(
                id = 3,
                firstName = "Elena",
                lastName = "Ivanova",
                age = 18,
                weight = 60.7f,
                isCompletedStudies = false,
                subjects = listOf(3, 4, 5)
            )
        ),
        subjects = listOf(
            Subject(
                id = 1,
                name = "Math",
                description = "Very useful subject"
            ),
            Subject(
                id = 2,
                name = "English",
                description = "Another very important subject"
            ),
            Subject(
                id = 3,
                name = "Computer Science",
                description = "Study how computers work"
            ),
            Subject(
                id = 4,
                name = "Economics",
                description = "There is the ability to study various financial systems."
            ),
            Subject(
                id = 5,
                name = "Philosophy"
            )
        )
    )

    val json = jsonObj {
        array("students") {
            data.students.forEach { student ->
                obj {
                    int("id", student.id)
                    string("first_name", student.firstName)
                    string("last_name", student.lastName)
                    int("age", student.age)
                    float("weight", student.weight)
                    boolean("is_completed_studies", student.isCompletedStudies)
                    array("subjects") {
                        student.subjects.forEach { subjectId ->
                            int(subjectId)
                        }
                    }
                }
            }
        }

        array("subjects") {
            data.subjects.forEach { subject ->
                obj {
                    int("id", subject.id)
                    string("name", subject.name)
                    subject.description?.let { description ->
                        string("description", description)
                    }
                }
            }
        }
    }

    val jsonString = writeJson(json)

    println(jsonString)
}
