data class Student(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val weight: Float,
    val isCompletedStudies: Boolean,
    val subjects: List<Int>
)

data class Subject(
    val id: Int,
    val name: String,
    val description: String? = null
)

data class StudentsData(
    val students: List<Student>,
    val subjects: List<Subject>
)
