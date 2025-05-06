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
package com.geekorum.aboutoss.ui.material

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.geekorum.aboutoss.ui.common.OpenSourceLicensesViewModel
import kotlinx.serialization.Serializable

// should be private but we have a weird IllegalAccessException on Desktop
// IllegalAccessException: class kotlinx.serialization.internal.PlatformKt
// cannot access a member of class com.geekorum.aboutoss.ui.material.DependenciesList
// with modifiers "public static final"
@Serializable
internal object DependenciesList

@Serializable
private data class DependencyLicense(
    val dependency: String
)

/**
 * Display opensource licences using [androidx.navigation.NavHost]
 */
@Composable
fun OpenSourceDependenciesNavHost(
    openSourceLicensesViewModel: OpenSourceLicensesViewModel,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = DependenciesList, modifier = modifier) {
        composable<DependenciesList> {
            OpenSourceDependenciesListScreen(
                viewModel = openSourceLicensesViewModel,
                onDependencyClick = {
                    navController.navigate(DependencyLicense(it))
                },
                onUpClick = navigateUp
            )
        }
        composable<DependencyLicense> {
            val route = it.toRoute<DependencyLicense>()
            val dependency = route.dependency
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
