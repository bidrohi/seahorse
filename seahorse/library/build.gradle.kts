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
    androidTarget {
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_1_8.toString()
            }
        }
    }

    val frameworkName = "Seahorse"
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
            linkerOpts.add("-lsqlite3")
            export(libs.nsexception)
            export(project(":seahorse:core"))
            export(project(":seahorse:ktor"))
            export(project(":seahorse:okhttp"))
            export(project(":seahorse:sqlite"))
        }
    }

    jvm()

    linuxX64()
    linuxArm64()

    mingwX64()

    sourceSets {
        commonMain.dependencies {
            api(project(":seahorse:core"))
            api(project(":seahorse:ktor"))
            api(project(":seahorse:okhttp"))
            api(project(":seahorse:sqlite"))
        }
    }
}

android {
    namespace = "com.bidyut.tech.seahorse"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

mavenPublishing {
    coordinates(
        groupId = libNamespace,
        artifactId = "seahorse-full",
        version = libVersion,
    )

    pom {
        name = "Seahorse Full"
        url = "https://github.com/bidrohi/seahorse"
        inceptionYear = "2023"
        description = """
            Seahorse provides a simple framework to support getting strings from various sources or fallback to the ones compiled into the app.
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
