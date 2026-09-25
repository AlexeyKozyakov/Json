import io.github.alexeykozyakov.json.builder.jsonObj
import io.github.alexeykozyakov.json.writer.writeJson

fun main() {
    val json = jsonObj {
        array("students") {
            obj {
                int("id", 1)
                string("first_name", "John")
                string("last_name", "Doe")
                int("age", 22)
                float("weight", 88.3f)
                boolean("is_completed_studies", false)

                array("subjects") {
                    int(1)
                    int(3)
                    int(5)
                }
            }

            obj {
                int("id", 2)
                string("first_name", "Alexey")
                string("last_name", "Kozyakov")
                int("age", 28)
                float("weight", 75.1f)
                boolean("is_completed_studies", true)

                array("subjects") {
                    int(1)
                    int(2)
                }
            }

            obj {
                int("id", 3)
                string("first_name", "Elena")
                string("last_name", "Ivanova")
                int("age", 18)
                float("weight", 60.7f)
                boolean("is_completed_studies", false)

                array("subjects") {
                    int(3)
                    int(4)
                    int(5)
                }
            }
        }

        array("subjects") {
            obj {
                int("id", 1)
                string("name", "Math")
                string("description", "Very useful subject")
            }

            obj {
                int("id", 2)
                string("name", "English")
                string("description", "Another very important subject")
            }

            obj {
                int("id", 3)
                string("name", "Computer Science")
                string("description", "Study how computers work")
            }

            obj {
                int("id", 4)
                string("name", "Economics")
                string("description", "There is the ability to study various financial systems.")
            }

            obj {
                int("id", 5)
                string("name", "Philosophy")
            }
        }
    }

    val jsonString = writeJson(json)

    println(jsonString)
}
