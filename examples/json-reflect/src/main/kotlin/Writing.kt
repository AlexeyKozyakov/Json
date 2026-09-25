import io.github.alexeykozyakov.json.reflect.writer.toJson

fun main() {
    val user = User(
        id = 12345,
        username = "alexey",
        email = "alexey@example.com",
        active = true,
        age = 28,
        balance = 1542.75,
        score = -12.5,
        avatar = null,

        profile = Profile(
            firstName = "Alexey",
            lastName = "Kozyakov",
            birthDate = "1998-04-15",
            location = Location(
                city = "Novosibirsk",
                country = "Russia",
                coordinates = Coordinates(
                    latitude = 55.0084,
                    longitude = 82.9357
                )
            )
        ),

        settings = Settings(
            notifications = true,
            darkMode = false,
            language = "en",
            theme = null,
            privacy = Privacy(
                showEmail = false,
                showProfile = true
            )
        ),

        roles = listOf(
            "user",
            "developer",
            "admin"
        ),

        statistics = Statistics(
            loginCount = 42,
            postCount = 17,
            rating = 4.95,
            achievements = listOf(
                Achievement(
                    id = 1,
                    name = "First Login",
                    unlocked = true,
                    points = 10
                ),
                Achievement(
                    id = 2,
                    name = "Early Adopter",
                    unlocked = true,
                    points = 50
                ),
                Achievement(
                    id = 3,
                    name = "Contributor",
                    unlocked = false,
                    points = 100
                )
            )
        ),

        projects = listOf(
            Project(
                id = 101,
                name = "JsonParser",
                description = "Lightweight JSON parser for Kotlin/JVM",
                stars = 128,
                forks = 14,
                language = "Kotlin",
                topics = listOf(
                    "kotlin",
                    "json",
                    "parser",
                    "jvm"
                ),
                repository = Repository(
                    url = "https://github.com/example/JsonParser",
                    private = false
                )
            ),
            Project(
                id = 102,
                name = "Snake",
                description = null,
                stars = 57,
                forks = 6,
                language = "Kotlin",
                topics = emptyList(),
                repository = Repository(
                    url = "https://github.com/example/Snake",
                    private = true
                )
            )
        ),

        recentActivity = listOf(
            Activity(
                type = "login",
                timestamp = "2026-09-25T10:15:32Z",
                success = true,
                metadata = null
            ),
            Activity(
                type = "purchase",
                timestamp = "2026-09-24T18:42:10Z",
                success = true,
                metadata = ActivityMetadata(
                    productId = "premium",
                    price = 9.99,
                    currency = "USD"
                )
            ),
            Activity(
                type = "error",
                timestamp = "2026-09-23T07:11:05Z",
                success = false,
                metadata = ActivityMetadata(
                    code = 500,
                    message = "Internal server error",
                    retryable = true
                )
            )
        ),

        preferences = Preferences(
            favoriteNumbers = listOf(
                1,
                3,
                7,
                42
            ),
            emptyList = emptyList(),
            nullableValues = listOf(
                null,
                "value",
                null
            )
        )
    )

    val json = user.toJson()

    println(json)
}
