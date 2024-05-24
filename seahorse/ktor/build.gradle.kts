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

    iosX64()
    iosArm64()
    iosSimulatorArm64()
    macosX64()
    macosArm64()
    watchosX64()
    watchosArm32()
    watchosArm64()
    watchosSimulatorArm64()
    tvosX64()
    tvosArm64()
    tvosSimulatorArm64()

    jvm()

    linuxX64()
    linuxArm64()

    mingwX64()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":seahorse:core"))

            implementation(libs.kotlinx.coroutines.core)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.encoding)
            implementation(libs.ktor.client.contentNavigation)
            implementation(libs.ktor.serialization.json)
        }
        commonTest.dependencies {
            implementation(libs.test.kotlin)
            implementation(libs.ktor.client.mock)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.android)
        }
        appleMain.dependencies {
            implementation(libs.ktor.client.darwin)
            api(libs.nsexception)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.client.cio)
        }
        linuxMain.dependencies {
            implementation(libs.ktor.client.cio)
        }
        mingwMain.dependencies {
            implementation(libs.ktor.client.winhttp)
        }
    }
}

android {
    namespace = "com.bidyut.tech.seahorse.data.ktor"
}

mavenPublishing {
    coordinates(
        groupId = libNamespace,
        artifactId = "seahorse-ktor",
        version = libVersion,
    )

    pom {
        name = "Seahorse Ktor Extension"
        url = "https://github.com/bidrohi/seahorse"
        inceptionYear = "2023"
        description = """
            Seahorse Ktor network extension.
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
