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
package com.geekorum.aboutoss.ui.material3

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.geekorum.aboutoss.common.generated.resources.Res
import com.geekorum.aboutoss.common.generated.resources.title_oss_licenses
import com.geekorum.aboutoss.core.licensee.LicenseeLicenseInfoRepository
import com.geekorum.aboutoss.ui.common.Factory
import com.geekorum.aboutoss.ui.common.OpenSourceLicensesViewModel
import org.jetbrains.compose.resources.stringResource

@Composable
fun OpenSourceLicensesWindow(
    onCloseRequest: () -> Unit,
    state: WindowState = rememberWindowState(),
) {
    val licenseInfoRepository =  remember {
        LicenseeLicenseInfoRepository()
    }
    val viewModel: OpenSourceLicensesViewModel = viewModel(factory = OpenSourceLicensesViewModel.Factory(licenseInfoRepository))
    OpenSourceLicensesWindow(onCloseRequest, state, viewModel)
}

@Composable
fun OpenSourceLicensesWindow(
    onCloseRequest: () -> Unit,
    state: WindowState = rememberWindowState(),
    viewModel: OpenSourceLicensesViewModel,
) {
    val title = stringResource(Res.string.title_oss_licenses)
    Window(onCloseRequest = onCloseRequest, state = state, title = title) {
        OpenSourceDependenciesNavHost(
            openSourceLicensesViewModel = viewModel,
            navigateUp = {
                onCloseRequest()
            }
        )
    }
}