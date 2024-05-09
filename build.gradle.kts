plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.kotlin.android).apply(false)
    alias(libs.plugins.kotlin.multiplatform).apply(false)
    alias(libs.plugins.kotlin.serialization).apply(false)
    alias(libs.plugins.kotlinx.kover).apply(false)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.sqldelight).apply(false)
    alias(libs.plugins.vanniktech.publish).apply(false)
}

val libNamespace by rootProject.extra { "com.bidyut.tech.seahorse" }
val libVersion by rootProject.extra { "0.6.0" }

tasks.register(
    "clean",
    Delete::class
) {
    delete(rootProject.layout.buildDirectory)
}

allprojects {
    layout.buildDirectory = File("${rootDir}/build/${projectDir.relativeTo(rootDir)}")
}
