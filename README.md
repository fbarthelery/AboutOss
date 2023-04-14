AboutOss
==========

AboutOss is an utility library to retrieve and display opensource licenses in Android applications.

Usage
=====

The library works with the [OSS Licenses Gradle Plugin](https://github.com/google/play-services-plugins/tree/master/oss-licenses-plugin). 
You can integrate it in your application with few simple steps.

### Apply the OSS Licenses Gradle Plugin

In your app-level `build.gradle`, apply the plugin by adding the following line under the existing `apply plugin: 'com.android.application'` at the top of the file:

```build.gradle.kts
apply plugin: 'com.google.android.gms.oss-licenses-plugin'
```

### Add the ui library to your application

```build.gradle.kts
repositories {
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation("com.geekorum.aboutoss:ui-material:0.0.1")
}
```

### Launch the license activity

```
val intent = Intent(this, OpenSourceLicensesActivity::class.java)
startActivity(intent)
```

Build instructions
==================

Just use Gradle to build

    ./gradlew build


License
=======

AboutOss is an open source library and is licensed under the GNU General Public License 3 and any later version.
This means that you can get AboutOss's code and modify it to suit your needs, as long as you publish the changes
you make for everyone to benefit from as well.

AboutOss is built and maintained by community volunteers.
