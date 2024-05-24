import com.android.build.api.dsl.CommonExtension
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.kotlin.android).apply(false)
    alias(libs.plugins.kotlin.multiplatform).apply(false)
    alias(libs.plugins.kotlin.compose).apply(false)
    alias(libs.plugins.kotlin.serialization).apply(false)
    alias(libs.plugins.kotlinx.kover).apply(false)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.sqldelight).apply(false)
    alias(libs.plugins.vanniktech.publish).apply(false)
}

val libNamespace by rootProject.extra { "com.bidyut.tech.seahorse" }
val libVersion by rootProject.extra { "0.9.0" }

allprojects {
    layout.buildDirectory = File("${rootDir}/build/${projectDir.relativeTo(rootDir)}")
}

val jvmVersion = JavaVersion.VERSION_11
val kotlinJvmTarget = JvmTarget.JVM_11

configure(subprojects) {
    tasks.withType<KotlinJvmCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(kotlinJvmTarget)
        }
    }
}

subprojects {
    afterEvaluate {
        (extensions.findByName("kotlin") as? KotlinMultiplatformExtension)?.apply {
            (targets.findByName("androidTarget") as? KotlinAndroidTarget)?.apply {
                publishLibraryVariants("release")
                @OptIn(ExperimentalKotlinGradlePluginApi::class)
                compilerOptions {
                    jvmTarget.set(kotlinJvmTarget)
                }
            }
        }
        (extensions.findByName("android") as? CommonExtension<*, *, *, *, *, *>)?.apply {
            compileSdk = 34
            buildToolsVersion = "34.0.0"
            defaultConfig {
                minSdk = 21
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
            compileOptions {
                sourceCompatibility = jvmVersion
                targetCompatibility = jvmVersion
            }
            lint {
                warningsAsErrors = true
            }
            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1}"
                }
            }
        }
    }
}
