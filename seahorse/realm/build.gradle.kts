import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.kover)
    alias(libs.plugins.realm)
    alias(libs.plugins.vanniktech.publish)
}

val libNamespace = rootProject.extra.get("libNamespace") as String
val libVersion = rootProject.extra.get("libVersion") as String
group = libNamespace
version = libVersion

kotlin {
    androidTarget()

    iosX64()
    iosArm64()
    iosSimulatorArm64()
    macosX64()
    macosArm64()

    jvm()

    sourceSets {
        commonMain.dependencies {
            api(project(":seahorse:core"))

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)

            implementation(libs.realm.base)
        }
        commonTest.dependencies {
            implementation(libs.test.kotlin)
        }
    }
}

android {
    namespace = "com.bidyut.tech.seahorse.data.realm"
}

mavenPublishing {
    coordinates(
        groupId = libNamespace,
        artifactId = "seahorse-realm",
        version = libVersion,
    )

    pom {
        name = "Seahorse Realm Extension"
        url = "https://github.com/bidrohi/seahorse"
        inceptionYear = "2024"
        description = """
            Seahorse Realm extension.
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
