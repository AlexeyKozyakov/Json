plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation(libs.vanniktech.maven.publish.plugin)
    implementation(libs.dokka.plugin)
}
