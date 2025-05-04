pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "com.google.android.gms.oss-licenses-plugin" -> useModule("com.google.android.gms:oss-licenses-plugin:${requested.version}")
            }
        }
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
include(":ui:common")
include(":ui:material2")
include(":ui:material3")
include(":sample")
include(":dokka")

