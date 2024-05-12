import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
}

val libNamespace = rootProject.extra.get("libNamespace") as String
val libVersion = rootProject.extra.get("libVersion") as String
group = libNamespace

kotlin {
    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            implementation(project(":seahorse:core"))
            implementation(project(":seahorse:ktor"))
            implementation(project(":seahorse:sqlite"))

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.compose.navigation)
            implementation(libs.compose.viewmodel)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.animation)
            implementation(compose.material3)
            implementation(compose.ui)

            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.ktor.client.core)
        }
        val desktopMain by getting
        desktopMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)

            implementation(compose.desktop.currentOs)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.ktor.client.cio)
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.bidyut.tech.seahorse.example"
            packageVersion = "1.0.0"
        }
    }
}
