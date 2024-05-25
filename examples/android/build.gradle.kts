import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import kotlinx.kover.gradle.plugin.dsl.GroupingEntityType

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinx.kover)
}

android {
    namespace = "com.bidyut.tech.seahorse.example"
    defaultConfig {
        applicationId = "com.bidyut.tech.seahorse.example"
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":seahorse:core"))
    implementation(project(":seahorse:ktor"))
    implementation(project(":seahorse:okhttp"))
    implementation(project(":seahorse:sqlite"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.work.runtime)

    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.compose.ui.graphics)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.toolingPreview)
    implementation(libs.compose.material3)

    implementation(libs.androidx.core)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.navigation)

    kover(project(":seahorse:core"))
    kover(project(":seahorse:ktor"))
    kover(project(":seahorse:okhttp"))
    kover(project(":seahorse:sqlite"))
}

kover {
    reports {
        filters {
            excludes {
                classes(
                    // Skip example code
                    "*.example.*",
                )
            }
        }
        total {
            verify {
                onCheck = true
                rule {
                    groupBy = GroupingEntityType.APPLICATION
                    bound {
                        minValue = 72
                        coverageUnits = CoverageUnit.LINE
                        aggregationForGroup = AggregationType.COVERED_PERCENTAGE
                    }
                }
            }
        }
    }
}
