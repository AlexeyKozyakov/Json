import com.vanniktech.maven.publish.MavenPublishBaseExtension

plugins {
    id("com.vanniktech.maven.publish")
}

extensions.configure<MavenPublishBaseExtension> {
    publishToMavenCentral()
    signAllPublications()

    pom {
        url = "https://github.com/AlexeyKozyakov/Json"

        licenses {
            license {
                name = "Apache License 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "repo"
            }
        }

        developers {
            developer {
                id = "alexey-kozyakov"
                name = "Aleksei Koziakov"
            }
        }

        scm {
            url = "https://github.com/AlexeyKozyakov/Json"
            connection =
                "scm:git:git://github.com/AlexeyKozyakov/Json.git"
            developerConnection =
                "scm:git:ssh://git@github.com/AlexeyKozyakov/Json.git"
        }
    }
}
