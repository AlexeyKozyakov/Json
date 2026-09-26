import io.github.alexeykozyakov.json.representation.JsonObject

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val active: Boolean,
    val age: Int,
    val balance: Double,
    val score: Double,
    val avatar: String?,
    val profile: Profile,
    val settings: Settings,
    val roles: List<String>,
    val statistics: Statistics,
    val projects: List<Project>,
    val recentActivity: List<Activity>,
    val preferences: Preferences,
    val additionalData: Map<String, Any>,
    val arbitraryObject: JsonObject
)

data class Profile(
    val firstName: String,
    val lastName: String,
    val birthDate: String,
    val location: Location
)

data class Location(
    val city: String,
    val country: String,
    val coordinates: Coordinates
)

data class Coordinates(
    val latitude: Double,
    val longitude: Double
)

data class Settings(
    val notifications: Boolean,
    val darkMode: Boolean,
    val language: String,
    val theme: String?,
    val privacy: Privacy
)

data class Privacy(
    val showEmail: Boolean,
    val showProfile: Boolean
)

data class Statistics(
    val loginCount: Int,
    val postCount: Int,
    val rating: Double,
    val achievements: List<Achievement>
)

data class Achievement(
    val id: Int,
    val name: String,
    val unlocked: Boolean,
    val points: Int
)

data class Project(
    val id: Int,
    val name: String,
    val description: String?,
    val stars: Int,
    val forks: Int,
    val language: String,
    val topics: List<String>,
    val repository: Repository
)

data class Repository(
    val url: String,
    val private: Boolean
)

data class Activity(
    val type: String,
    val timestamp: String,
    val success: Boolean,
    val metadata: ActivityMetadata?
)

data class ActivityMetadata(
    val productId: String? = null,
    val price: Double? = null,
    val currency: String? = null,
    val code: Int? = null,
    val message: String? = null,
    val retryable: Boolean? = null
)

data class Preferences(
    val favoriteNumbers: List<Int>,
    val emptyList: List<String>,
    val nullableValues: List<String?>
)
