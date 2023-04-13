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
        // for geekdroid
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

rootProject.name = "AboutOss"
include(":core")
include(":ui")
