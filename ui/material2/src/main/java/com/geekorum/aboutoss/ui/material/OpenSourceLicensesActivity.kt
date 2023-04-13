/*
 * AboutOss is a utility library to retrieve and display
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
package com.geekorum.aboutoss.ui.material

import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.geekorum.aboutoss.ui.common.BaseOpensourceLicenseActivity
import com.geekorum.aboutoss.ui.common.OpenSourceLicensesViewModel

class OpenSourceLicensesActivity : BaseOpensourceLicenseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        var themeProvider: @Composable (@Composable () -> Unit) -> Unit = { content ->
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
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }
    }
}

