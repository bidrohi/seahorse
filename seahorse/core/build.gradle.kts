import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

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

    val frameworkName = "SeahorseCore"
    val xcf = XCFramework(frameworkName)
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
        macosX64(),
        macosArm64(),
        watchosX64(),
        watchosArm32(),
        watchosArm64(),
        watchosSimulatorArm64(),
        tvosX64(),
        tvosArm64(),
        tvosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = frameworkName
            xcf.add(this)
            isStatic = true
        }
    }

    jvm()

    linuxX64()
    linuxArm64()

    mingwX64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
        }
        commonTest.dependencies {
            implementation(libs.test.kotlin)
        }
        androidMain.dependencies {
            implementation(libs.androidx.work.runtime)
            implementation(libs.androidx.preferences)
        }
        val androidUnitTest by getting
        androidUnitTest.dependencies {
            implementation(libs.test.robolectric)
            implementation(libs.test.androidx.work)
            implementation(libs.test.mockk)
        }
    }
}

android {
    namespace = "com.bidyut.tech.seahorse.core"
}

mavenPublishing {
    coordinates(
        groupId = libNamespace,
        artifactId = "seahorse-core",
        version = libVersion,
    )

    pom {
        name = "Seahorse Core"
        url = "https://github.com/bidrohi/seahorse"
        inceptionYear = "2023"
        description = """
            Seahorse provides a simple framework to support getting strings from various sources or fallback to the ones compiled into the app.
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

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()
}
