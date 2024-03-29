pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Movie"
include(
    ":app",

    ":core:data",
    ":core:domain",
    ":core:ui",
    ":core:datastore",
    ":core:testing",

    ":feature:main",
    ":feature:home",
    ":feature:popular",
    ":feature:detail"
)
