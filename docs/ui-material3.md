Ui-Material3
============

Setup
=====

Add the dependency to your project

```kotlin title="build.gradle.kts"
dependencies {
    implementation("com.geekorum.aboutoss:ui-material3:<latest-version>")
}
```

Usage
=====

The [OpenSourceDependenciesNavHost](api/ui/material3/com.geekorum.aboutoss.ui.material3/-open-source-dependencies-nav-host.html)
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

The [AdaptiveOpenSourceDependenciesScreen](api/ui/material3/com.geekorum.aboutoss.ui.material3/-adaptive-open-source-dependencies-screen.html)
composable display the licenses in an adaptive screen. The User interface will adapt based on the available space.

```kotlin
val licenseInfoRepository = LicenseeLicenseInfoRepository()
val viewModel = viewModel<OpenSourceLicensesViewModel>(factory = OpenSourceLicensesViewModel.Factory(licenseInfoRepository))
AdaptiveOpenSourceDependenciesScreen(
    openSourceLicensesViewModel = viewModel,
    navigateUp = {
        // close screen
    }
)
```


Android
=======

The [OpenSourceLicensesActivity](api/ui/material3/com.geekorum.aboutoss.ui.material3/-open-source-licenses-activity/index.html) is configured to work with the [OSS Licenses Gradle Plugin](https://github.com/google/play-services-plugins/tree/main/oss-licenses-plugin)
You can launch the activity like this:

```kotlin
val intent = Intent(this, OpenSourceLicensesActivity::class.java)
startActivity(intent)
```


Desktop
=======

On Desktop the [OpenSourceLicensesWindow](api/ui/material3/com.geekorum.aboutoss.ui.material3/-open-source-licenses-window.html) is configured to work with [licensee](https://github.com/cashapp/licensee).
You can use it like this:

```kotlin
OpenSourceLicensesWindow(onCloseRequest = {
    // close window
})
```

