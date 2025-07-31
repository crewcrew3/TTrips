enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "T-Trips"
include(":app")
include(":core:data")
include(":core:ui")
include(":core:navigation")
include(":core:network")
include(":core:domain")
include(":core:utils")
include(":feature:authentication")
include(":core:navigation-api")
include(":feature:triplist")
include(":feature:profile")
include(":feature:triparchive")
include(":feature:editprofile")
include(":feature:addtrip")
include(":feature:tripinfo")
include(":feature:looktripmembers")
include(":feature:notifications")
include(":core:common")
include(":feature:invitationdetails")
include(":feature:addactualexpense")
include(":feature:actualexpenseinfo")
include(":feature:addplannedexpense")
include(":feature:reporttrip")
