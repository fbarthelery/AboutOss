/*
 * AboutOss is an utility library to retrieve and display
 * opensource licenses in Android applications.
 *
 * Copyright (C) 2023-2025 by Frederic-Charles Barthelery.
 *
 * This file is part of AboutOss.
 *
 * AboutOss is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AboutOss is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AboutOss.  If not, see <http://www.gnu.org/licenses/>.
 */

plugins {
    `kotlin-dsl`
}


version = "1.0"

kotlin{
    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.RequiresOptIn")
            }
        }
    }
}
repositories {
    google {
        content {
            includeGroupByRegex("""android\.arch\..*""")
            includeGroupByRegex("""androidx\..*""")
            includeGroupByRegex("""com\.android\..*""")
            includeGroupByRegex("""com\.google\..*""")
            includeGroup("com.crashlytics.sdk.android")
            includeGroup("io.fabric.sdk.android")
            includeGroup("org.chromium.net")
            includeGroup("zipflinger")
            includeGroup("com.android")
    	}
    }
    mavenCentral()
    gradlePluginPortal()
}

// see https://github.com/gradle/gradle/issues/17963
fun Provider<PluginDependency>.gav(): String {
    val t = get()
    val id = t.pluginId
    val version = t.version
    return "$id:$id.gradle.plugin:$version"
}

dependencies {
    implementation(libs.plugins.android.application.gav())
    implementation(libs.plugins.kotlin.android.gav())
    implementation("gradle.plugin.com.hierynomus.gradle.plugins:license-gradle-plugin:0.16.1")
    implementation(libs.plugins.vanniktech.maven.publish.gav())
    implementation("com.geekorum.gradle.avdl:plugin:0.0.3")
    implementation("com.geekorum.gradle.avdl:flydroid:0.0.3")
}
