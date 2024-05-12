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

rootProject.name = "Seahorse Library"

include(":androidExample")
include(":desktopExample")
include(":seahorse:core")
include(":seahorse:ktor")
include(":seahorse:library")
include(":seahorse:okhttp")
include(":seahorse:sqlite")
