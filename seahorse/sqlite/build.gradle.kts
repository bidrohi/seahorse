import com.vanniktech.maven.publish.SonatypeHost

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
            api(project(":seahorse:core"))

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)

            implementation(libs.sqldelight.runtime)
        }
        androidMain.dependencies {
            implementation(libs.sqldelight.android)
        }
        appleMain.dependencies {
            implementation(libs.sqldelight.native)
            api(libs.nsexception)
        }
        nativeMain.dependencies {
            implementation(libs.sqldelight.native)
        }
        jvmMain.dependencies {
            implementation(libs.sqldelight.sqlite)
        }
        jvmTest.dependencies {
            implementation(libs.test.kotlin)
        }
    }
}

android {
    namespace = "com.bidyut.tech.seahorse.data.sql"
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
        artifactId = "seahorse-sqlite",
        version = libVersion,
    )

    pom {
        name = "Seahorse SQLite Extension"
        url = "https://github.com/bidrohi/seahorse"
        inceptionYear = "2023"
        description = """
            Seahorse SQLite extension.
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
