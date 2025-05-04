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
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("com.geekorum.build.source-license-checker")
    alias(libs.plugins.jetbrains.compose.multiplatform)
    alias(libs.plugins.kotlin.compose)
    id("com.geekorum.build.maven-publish")
    id("com.geekorum.build.dokka")
}

group = "com.geekorum.aboutoss"
version = "0.1.0"

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "aboutoss-ui-common"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core"))
            api(libs.jetbrains.androidx.lifecycle.runtimeCompose)
            api(libs.jetbrains.androidx.lifecycle.viewmodel)
            api(compose.components.resources)
            implementation(compose.runtime)
        }

        androidMain.dependencies {
            api(libs.androidx.activity)
            implementation(libs.androidx.activity.compose)
        }
    }
}

compose.resources {
    publicResClass = true
}

android {
    namespace = "com.geekorum.aboutoss.ui.common"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        aarMetadata {
            minCompileSdk = 24
        }

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
    "androidMainApi"(libs.geekdroid) {
        exclude("androidx.compose.material3")
    }
}

mavenPublishing {
    val artifactId = "ui-common"
    coordinates(groupId = group.toString(), artifactId, version.toString())
    pom {
        name = artifactId
    }
}

dokka {
    moduleName = "ui-common"
}
