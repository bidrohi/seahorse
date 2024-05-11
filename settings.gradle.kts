pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SeahorseLibrary"

include(":examples:android")
include(":examples:jvm_cli")
include(":seahorse:core")
include(":seahorse:ktor")
include(":seahorse:library")
include(":seahorse:okhttp")
include(":seahorse:realm")
include(":seahorse:sqlite")
