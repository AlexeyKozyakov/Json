plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.dokka)
}

allprojects {
    group = "io.github.alexeykozyakov.json"
    version = "1.0.8"
}

dependencies {
    dokka(project(":json"))
    dokka(project(":json-reflect"))
}
