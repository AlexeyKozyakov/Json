import io.github.alexeykozyakov.json.accessors.obj
import io.github.alexeykozyakov.json.accessors.string
import io.github.alexeykozyakov.json.builder.jsonObj
import io.github.alexeykozyakov.json.reflect.JsonMapper
import io.github.alexeykozyakov.json.reflect.parser.fromJson
import io.github.alexeykozyakov.json.reflect.parser.fromJsonRepresentation
import io.github.alexeykozyakov.json.reflect.writer.toJson
import io.github.alexeykozyakov.json.reflect.writer.toJsonRepresentation
import io.github.alexeykozyakov.json.representation.Json

sealed interface Shape {
    companion object : JsonMapper<Shape> {
        override fun toJson(value: Shape): Json {
            return when (value) {
                is Circle -> jsonObj {
                    string("type", "circle")
                    fields(value.toJsonRepresentation().obj())
                }

                is Rectangle -> jsonObj {
                    string("type", "rectangle")
                    fields(value.toJsonRepresentation().obj())
                }

                is Square -> jsonObj {
                    string("type", "square")
                    fields(value.toJsonRepresentation().obj())
                }
            }
        }

        override fun fromJson(json: Json): Shape {
            return when (val type = json.string("type")) {
                "circle" -> fromJsonRepresentation<Circle>(json)
                "rectangle" -> fromJsonRepresentation<Rectangle>(json)
                "square" -> fromJsonRepresentation<Square>(json)
                else -> error("Unsupported shape type $type")
            }
        }
    }
}

data class Circle(
    val center: Point,
    val radius: Double
) : Shape

data class Rectangle(
    val topLeft: Point,
    val width: Double,
    val height: Double
) : Shape

data class Square(
    val topLeft: Point,
    val size: Double
) : Shape

data class Point(
    val x: Double,
    val y: Double
)

fun main() {
    val json = """
        [
            {
                "type": "circle",
                "center": {
                    "x": 100.0,
                    "y": 150.0
                },
                "radius": 50.0
            },
            {
                "type": "rectangle",
                "topLeft": {
                    "x": 20.0,
                    "y": 30.0
                },
                "width": 5.0,
                "height": 10.0
            },
            {
                "type": "square",
                "topLeft": {
                    "x": 300.0,
                    "y": 600.0
                },
                "size": 20.0
            }
        ]
    """.trimIndent()

    val shapes = fromJson<List<Shape>>(json)

    println(shapes)

    val sameJson = shapes.toJson()

    println(sameJson)
}
