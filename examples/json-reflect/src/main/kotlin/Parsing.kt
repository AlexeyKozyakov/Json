import io.github.alexeykozyakov.json.reflect.parser.fromJson

fun main() {
    val json = """
        {
          "id": 12345,
          "username": "alexey",
          "email": "alexey@example.com",
          "active": true,
          "age": 28,
          "balance": 1542.75,
          "score": -12.5,
          "avatar": null,

          "profile": {
            "firstName": "Alexey",
            "lastName": "Kozyakov",
            "birthDate": "1998-04-15",
            "location": {
              "city": "Novosibirsk",
              "country": "Russia",
              "coordinates": {
                "latitude": 55.0084,
                "longitude": 82.9357
              }
            }
          },

          "settings": {
            "notifications": true,
            "darkMode": false,
            "language": "en",
            "theme": null,
            "privacy": {
              "showEmail": false,
              "showProfile": true
            }
          },

          "roles": [
            "user",
            "developer",
            "admin"
          ],

          "statistics": {
            "loginCount": 42,
            "postCount": 17,
            "rating": 4.95,
            "achievements": [
              {
                "id": 1,
                "name": "First Login",
                "unlocked": true,
                "points": 10
              },
              {
                "id": 2,
                "name": "Early Adopter",
                "unlocked": true,
                "points": 50
              },
              {
                "id": 3,
                "name": "Contributor",
                "unlocked": false,
                "points": 100
              }
            ]
          },

          "projects": [
            {
              "id": 101,
              "name": "JsonParser",
              "description": "Lightweight JSON parser for Kotlin/JVM",
              "stars": 128,
              "forks": 14,
              "language": "Kotlin",
              "topics": [
                "kotlin",
                "json",
                "parser",
                "jvm"
              ],
              "repository": {
                "url": "https://github.com/example/JsonParser",
                "private": false
              }
            },
            {
              "id": 102,
              "name": "Snake",
              "description": null,
              "stars": 57,
              "forks": 6,
              "language": "Kotlin",
              "topics": [],
              "repository": {
                "url": "https://github.com/example/Snake",
                "private": true
              }
            }
          ],

          "recentActivity": [
            {
              "type": "login",
              "timestamp": "2026-09-25T10:15:32Z",
              "success": true,
              "metadata": null
            },
            {
              "type": "purchase",
              "timestamp": "2026-09-24T18:42:10Z",
              "success": true,
              "metadata": {
                "productId": "premium",
                "price": 9.99,
                "currency": "USD"
              }
            },
            {
              "type": "error",
              "timestamp": "2026-09-23T07:11:05Z",
              "success": false,
              "metadata": {
                "code": 500,
                "message": "Internal server error",
                "retryable": true
              }
            }
          ],

          "preferences": {
            "favoriteNumbers": [
              1,
              3,
              7,
              42
            ],
            "emptyList": [],
            "nullableValues": [
              null,
              "value",
              null
            ]
          },
          "additionalData": {
            "favoriteArtists": [
               "Lil Peep",
               "XXXTentacion"
            ],
            "clothesSize": "XXL"
          },
          "arbitraryObject": {
            "code": 67,
            "data": "six seven"
          }
        }
    """.trimIndent()

    val user = fromJson<User>(json)

    println(user)
}
