import io.github.alexeykozyakov.json.reflect.JsonModel
import io.github.alexeykozyakov.json.reflect.JsonName
import io.github.alexeykozyakov.json.reflect.JsonSkip
import io.github.alexeykozyakov.json.representation.JsonObject

data class User(
    val id: Int,
    val username: String,
    @JsonName("first_name")
    val firstName: String,
    @JsonName("last_name")
    val lastName: String,
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
    val arbitraryObject: JsonObject,
    val defaultValue: Int = 3,
    @JsonSkip
    val skippableValue: Float
) : JsonModel

data class Profile(
    val firstName: String,
    val lastName: String,
    val birthDate: String,
    val location: Location
) : JsonModel

data class Location(
    val city: String,
    val country: String,
    val coordinates: Coordinates
) : JsonModel

data class Coordinates(
    val latitude: Double,
    val longitude: Double
) : JsonModel

data class Settings(
    val notifications: Boolean,
    val darkMode: Boolean,
    val language: String,
    val theme: String?,
    val privacy: Privacy
) : JsonModel

data class Privacy(
    val showEmail: Boolean,
    val showProfile: Boolean
) : JsonModel

data class Statistics(
    val loginCount: Int,
    val postCount: Int,
    val rating: Double,
    val achievements: List<Achievement>
) : JsonModel

data class Achievement(
    val id: Int,
    val name: String,
    val unlocked: Boolean,
    val points: Int
) : JsonModel

data class Project(
    val id: Int,
    val name: String,
    val description: String?,
    val stars: Int,
    val forks: Int,
    val language: String,
    val topics: List<String>,
    val repository: Repository
) : JsonModel

data class Repository(
    val url: String,
    val private: Boolean
) : JsonModel

data class Activity(
    val type: String,
    val timestamp: String,
    val success: Boolean,
    val metadata: ActivityMetadata?
) : JsonModel

data class ActivityMetadata(
    val productId: String? = null,
    val price: Double? = null,
    val currency: String? = null,
    val code: Int? = null,
    val message: String? = null,
    val retryable: Boolean? = null
) : JsonModel

data class Preferences(
    val favoriteNumbers: List<Int>,
    val emptyList: List<String>,
    val nullableValues: List<String?>
) : JsonModel
