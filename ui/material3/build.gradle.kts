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
    id("com.geekorum.build.conventions.mpp-library-with-android")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.compose.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
    id("com.geekorum.build.maven-publish")
    id("com.geekorum.build.dokka")
}

kotlin {
    androidTarget()

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "aboutoss-ui-material3"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":ui:common"))
            implementation(project(":core"))
            implementation(compose.material3)
            implementation(libs.jetbrains.compose.materialIconsCore)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.jetbrains.androidx.navigation.compose)
            implementation(libs.jetbrains.compose.material3AdaptiveNavigation)
            implementation(libs.jetbrains.compose.uiBackhandler)
            implementation(libs.jetbrains.androidx.lifecycle.viewModelCompose)
        }

        androidMain.dependencies {
            api(libs.androidx.activity)
            implementation(dependencies.platform(libs.androidx.compose.bom))
            implementation(libs.androidx.activity.compose)
        }
    }
}


android {
    namespace = "com.geekorum.aboutoss.ui.material3"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
    }

    publishing {
        singleVariant("release") {
            withJavadocJar()
            withSourcesJar()
        }
    }
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    afterEvaluate {
        "androidDebugImplementation"(libs.androidx.compose.uiTooling)
    }
}

mavenPublishing {
    val artifactId = "ui-material3"
    coordinates(groupId = group.toString(), artifactId, version.toString())
    pom {
        name = artifactId
    }
}

dokka {
    moduleName = "ui-material3"
}