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
package com.geekorum.aboutoss.sampleapp

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.geekorum.aboutoss.ui.common.OpenSourceLicensesViewModel
import com.geekorum.aboutoss.ui.material3.AdaptiveOpenSourceDependenciesScreen
import kotlinx.serialization.Serializable
import com.geekorum.aboutoss.ui.material.OpenSourceDependenciesNavHost as Material2OpenSourceDependenciesNavHost


@Serializable
private object Home

@Serializable
private object Material2

@Serializable
private object Material3


@Composable
fun SampleAppIos() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Home) {
        composable<Home> {
            SampleApp(
                onMaterial2Click = {
                    navController.navigate(Material2)
                },
                onMaterial3Click = {
                    navController.navigate(Material3)
                }
            )
        }
        composable<Material2> {
            Material2Screen(navigateUp = {
                navController.popBackStack()
            })
        }
        composable<Material3> {
            Material3Screen(navigateUp = {
                navController.popBackStack()
            })
        }
    }
}

@Composable
fun Material2Screen(navigateUp: () -> Unit) {
    val viewModel: OpenSourceLicensesViewModel = viewModel(initializer = {
        createPrebuildOpenSourceLicensesViewModel()
    })
    Material2OpenSourceDependenciesNavHost(
        openSourceLicensesViewModel = viewModel,
        navigateUp = navigateUp
    )
}

@Composable
fun Material3Screen(navigateUp: () -> Unit) {
    val viewModel: OpenSourceLicensesViewModel = viewModel(initializer = {
        createPrebuildOpenSourceLicensesViewModel()
    })
    AdaptiveOpenSourceDependenciesScreen(
        openSourceLicensesViewModel = viewModel,
        navigateUp = navigateUp
    )
}