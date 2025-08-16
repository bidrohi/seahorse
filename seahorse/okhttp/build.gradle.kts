plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlinx.kover)
    alias(libs.plugins.vanniktech.publish)
}

val libNamespace = rootProject.extra.get("libNamespace") as String
val libVersion = rootProject.extra.get("libVersion") as String
group = libNamespace
version = libVersion

kotlin {
    jvm()

    sourceSets {
        jvmMain.dependencies {
            implementation(project(":seahorse:core"))

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.okhttp.core)
        }
        jvmTest.dependencies {
            implementation(libs.test.kotlin)
            implementation(libs.test.okhttp.mockwebserver)
        }
    }
}

mavenPublishing {
    coordinates(
        groupId = libNamespace,
        artifactId = "seahorse-okhttp",
        version = libVersion,
    )

    pom {
        name = "Seahorse OkHttp Extension"
        url = "https://github.com/bidrohi/seahorse"
        inceptionYear = "2023"
        description = """
            Seahorse OkHttp network extension.
        """.trimIndent()

        licenses {
            license {
                name = "CC BY-SA 4.0"
                url = "https://creativecommons.org/licenses/by-sa/4.0/"
            }
        }
        developers {
            developer {
                id = "bidrohi"
                name = "Saud Khan"
                url = "https://github.com/bidrohi/"
            }
        }
        scm {
            url = "https://github.com/bidrohi/seahorse"
        }
    }

    publishToMavenCentral()

    signAllPublications()
}
