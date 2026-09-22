plugins {
    id("java-library")
    id("jsonparser.kotlin-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}
mavenPublishing {
    coordinates(
        artifactId = "json"
    )

    pom {
        name = "Json"
        description = "Simple and convenient JSON parser for kotlin"
    }
}
dependencies {
    testImplementation(libs.junit)
}
