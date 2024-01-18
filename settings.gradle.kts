pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven ( url = "https://devrepo.kakao.com/nexus/content/groups/public/" )
    }
}

rootProject.name = "cooktionary-android"
include(":presentation")
include(":domain")
include(":data")
