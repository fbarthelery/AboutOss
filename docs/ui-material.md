Ui-Material (Deprecated)
========================

The Material user interface will not receive further updates and is now deprecated.
Consider using [ui-material3](ui-material3.md) instead.

Setup
=====

Add the dependency to your project

```kotlin title="build.gradle.kts"
dependencies {
    implementation("com.geekorum.aboutoss:ui-material:<latest-version>")
}
```

Usage
=====

The [OpenSourceDependenciesNavHost](api/ui/material2/com.geekorum.aboutoss.ui.material/-open-source-dependencies-nav-host.html)
composable allows to display the licenses. 
It takes an [OpenSourceLicensesViewModel](api/ui/common/com.geekorum.aboutoss.ui.common/-open-source-licenses-view-model/index.html)
that you can create with the [LicenseInfoRepository](license-sources.md) of your choice.

```kotlin
val licenseInfoRepository = LicenseeLicenseInfoRepository()
val viewModel = viewModel<OpenSourceLicensesViewModel>(factory = OpenSourceLicensesViewModel.Factory(licenseInfoRepository))
OpenSourceDependenciesNavHost(
    openSourceLicensesViewModel = viewModel,
    navigateUp = {
        // close screen
    }
)
```

Android
=======

The [OpenSourceLicensesActivity](api/ui/material2/com.geekorum.aboutoss.ui.material/-open-source-licenses-activity/index.html) is configured to work with the [OSS Licenses Gradle Plugin](https://github.com/google/play-services-plugins/tree/main/oss-licenses-plugin)
You can launch the activity like this:

```kotlin
val intent = Intent(this, OpenSourceLicensesActivity::class.java)
startActivity(intent)
```


Desktop
=======

On Desktop the [OpenSourceLicensesWindow](api/ui/material2/com.geekorum.aboutoss.ui.material/-open-source-licenses-window.html) is configured to work with [licensee](https://github.com/cashapp/licensee).
You can use it like this:

```kotlin
OpenSourceLicensesWindow(onCloseRequest = {
    // close window
})
```

