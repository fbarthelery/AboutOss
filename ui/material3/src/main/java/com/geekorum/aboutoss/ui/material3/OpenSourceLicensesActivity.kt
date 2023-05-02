/*
 * AboutOss is an utility library to retrieve and display
 * opensource licenses in Android applications.
 *
 * Copyright (C) 2023 by Frederic-Charles Barthelery.
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
package com.geekorum.aboutoss.ui.material3

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.geekorum.aboutoss.ui.common.BaseOpensourceLicenseActivity
import com.geekorum.aboutoss.ui.common.OpenSourceLicensesViewModel

/**
 * Activity to display opensource license information
 *
 * This activity use Material compose to create the UI.
 * You can specify the Material theme to use by setting [themeProvider]
 * before launching the activity
 */
open class OpenSourceLicensesActivity : BaseOpensourceLicenseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            themeProvider {
                DependencyNavHost(
                    openSourceLicensesViewModel = viewModel,
                    navigateUp = {
                        if (!onNavigateUp()) {
                            finish()
                        }
                    }
                )
            }
        }
    }

    companion object {
        /**
         * The composable Theme function to set the theme of the UI in [OpenSourceLicensesActivity]
         * Default to base material theme [MaterialTheme]
         */
        var themeProvider: @Composable (@Composable () -> Unit) -> Unit = { content ->
            val darkTheme: Boolean = isSystemInDarkTheme()
            val colorScheme = MaterialTheme.colorScheme
            val view = LocalView.current
            if (!view.isInEditMode) {
                SideEffect {
                    val window = (view.context as Activity).window
                    window.statusBarColor = colorScheme.primary.toArgb()
                    WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
                }
            }
            MaterialTheme(content = content)
        }
    }
}

@Composable
fun DependencyNavHost(
    openSourceLicensesViewModel: OpenSourceLicensesViewModel,
    navigateUp: () -> Unit
) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "dependencies") {
        composable("dependencies") {
            OpenSourceDependenciesListScreen(
                viewModel = openSourceLicensesViewModel,
                onDependencyClick = {
                    navController.navigate("dependency_license/${Uri.encode(it)}")
                },
                onUpClick = navigateUp
            )
        }
        composable("dependency_license/{dependency}") {
            val dependency = requireNotNull(it.arguments?.getString("dependency"))
            OpenSourceLicenseScreen(
                viewModel = openSourceLicensesViewModel,
                dependency = dependency,
                onUpClick = {
                    navController.popBackStack()
                },
            )
        }
    }
}
