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

import androidx.lifecycle.viewmodel.CreationExtras
import com.geekorum.aboutoss.core.licensee.LicenseeLicenseInfoRepository
import com.geekorum.aboutoss.ui.common.DesktopBrowserLauncher
import com.geekorum.aboutoss.ui.common.OpenSourceLicensesViewModel
import kotlinx.coroutines.Dispatchers
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
actual fun CreationExtras.createPrebuildOpenSourceLicensesViewModel(): OpenSourceLicensesViewModel {
    val licenseInfoRepository = LicenseeLicenseInfoRepository(
        mainCoroutineDispatcher = Dispatchers.Main,
        ioCoroutineDispatcher = Dispatchers.IO,
        licenseeResourcePath = "app/cash/licensee/prebuilt_artifacts.json",
    )

    return OpenSourceLicensesViewModel(
        licenseInfoRepository,
        DesktopBrowserLauncher()
    )
}