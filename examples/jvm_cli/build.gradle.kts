import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = rootProject.extra.get("libNamespace") as String
version = rootProject.extra.get("libVersion") as String

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    implementation(project(":seahorse:core"))
    implementation(project(":seahorse:ktor"))
    implementation(project(":seahorse:sqlite"))
    implementation(project(":seahorse:realm"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
}
