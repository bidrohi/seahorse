import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.kover)
    alias(libs.plugins.vanniktech.publish)
}

val libNamespace = rootProject.extra.get("libNamespace") as String
val libVersion = rootProject.extra.get("libVersion") as String
group = libNamespace
version = libVersion

kotlin {
    androidTarget()

    sourceSets {
        androidMain.dependencies {
            implementation(project(":seahorse:core"))

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.okhttp.core)
        }
    }
}

android {
    namespace = "com.bidyut.tech.seahorse.data.okhttp"
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
                name = "CC BY-NC-SA 4.0"
                url = "https://creativecommons.org/licenses/by-nc-sa/4.0/"
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

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()
}
