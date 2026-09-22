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
        artifactId = "json-reflect"
    )

    pom {
        name = "Json Reflect"
        description = "Extended version of kotlin JSON parser which uses reflection under the hood"
    }
}

dependencies {
    implementation(project(":json"))
    api(kotlin("reflect"))
    testImplementation(libs.junit)
}
