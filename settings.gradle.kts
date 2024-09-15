pluginManagement {
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

rootProject.name = "COMP90018-Tutorials-2024"
include(":3-1-activitylifecycle")
include(":3-2-layoutdemo")
include(":3-3-watch")

include(":2-firstdemo")
include(":4-1-multithreads")
include(":4-2-services")
include(":5-1-sensor-barometer")
include(":5-2-sensor-gps")
include(":6-1-storage-sharedpreferences")
include(":6-2-storage-database")
include(":6-4-storage-contentprovider")
include(":6-5-storage-internalstorage")
include(":7-2-connectivity-firebase")
