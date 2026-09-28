package io.github.alexeykozyakov.json.reflect

/**
 * Marker interface that tells the compiler that given
 * class is used as JSON model.
 * Added to disable some code optimizations for DTO classes in
 * builds where ProGuard or R8 is used to ensure that
 * reflection JSON mapping will work fine.
 */
interface JsonModel
