plugins {
    id("com.android.library")
    kotlin("android")
    id("com.geekorum.build.source-license-checker")
    `maven-publish`
}

group = "com.geekorum.aboutoss"
version = "0.0.1"

android {
    namespace = "com.geekorum.aboutoss.ui.material"
    compileSdk = 33

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
    }

    publishing {
        singleVariant("release") {
            withJavadocJar()
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(project(":ui:common"))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

publishing {
    publications {
        val pomConfiguration: (MavenPom).() -> Unit = {
            name.set("ui-material")
            description.set("A library to retrieve and display opensource licenses in Android applications")
            licenses {
                license {
                    name.set("GPL-3.0-or-later")
                    url.set("https://www.gnu.org/licenses/gpl-3.0.html")
                    distribution.set("repo")
                }
            }
            inceptionYear.set("2023")
        }

        register<MavenPublication>("release") {
            afterEvaluate {
                from(components["release"])
            }
            artifactId = "ui-material"
            pom(pomConfiguration)
        }
    }
}
