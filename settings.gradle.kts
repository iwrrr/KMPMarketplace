rootProject.name = "KMPMarketplace"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

include(":composeApp")
include(":apis:authentication")
include(":apis:product")
include(":libraries:component")
include(":libraries:core")
include(":features:authentication")
include(":features:favorite")
include(":features:home")
include(":features:product_detail")
include(":features:product_list")
