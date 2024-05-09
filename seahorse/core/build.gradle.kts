import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.kover)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.vanniktech.publish)
}

val libNamespace = rootProject.extra.get("libNamespace") as String
val libVersion = rootProject.extra.get("libVersion") as String
group = libNamespace
version = libVersion

kotlin {
    applyDefaultHierarchyTemplate()

    androidTarget {
        publishLibraryVariants("release", "debug")
        compilations.all {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_1_8.toString()
            }
        }
    }

    val xcf = XCFramework("Seahorse")
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "Seahorse"
            xcf.add(this)
            isStatic = true
            export(libs.nsexception)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.encoding)
                implementation(libs.ktor.client.contentNavigation)
                implementation(libs.ktor.serialization.json)
                implementation(libs.sqldelight.runtime)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.ktor.client.mock)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.client.android)
                implementation(libs.okhttp.core)
                implementation(libs.androidx.work.runtime)
                implementation(libs.androidx.preferences)
                implementation(libs.sqldelight.android)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(libs.test.robolectric)
                implementation(libs.androidx.test.work)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines)
                implementation(libs.ktor.client.darwin)
                implementation(libs.sqldelight.native)
                api(libs.nsexception)
            }
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

sqldelight {
    databases {
        create("SeahorseStrings") {
            packageName.set("com.bidyut.tech.seahorse.data.sql")
        }
    }
}

mavenPublishing {
    coordinates(
        groupId = libNamespace,
        artifactId = project.name,
        version = libVersion,
    )

    pom {
        name = "Seahorse"
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
