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
package com.geekorum.aboutoss.ui.common

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.geekorum.aboutoss.core.LicenseInfoRepository
import com.geekorum.geekdroid.network.BrowserLauncher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Manage opensource license information and allow to display them in an UI
 */
class OpenSourceLicensesViewModel constructor(
    private val licenseInfoRepository: LicenseInfoRepository,
    private val browserLauncher: BrowserLauncher,
) : ViewModel() {
    init {
        browserLauncher.warmUp(null)
    }

    private val licensesInfo = flow {
        emit(licenseInfoRepository.getLicensesInfo())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val dependenciesList = licensesInfo.map { licensesInfo ->
        licensesInfo.keys.sortedBy { it.lowercase() }
    }

    fun getLicenseDependency(dependency: String) = flow {
        emit(licenseInfoRepository.getLicenseFor(dependency))
    }

    fun openLinkInBrowser(context: Context, link: String) {
        browserLauncher.launchUrl(context, link.toUri(),  null as BrowserLauncher.LaunchCustomizer?)
    }

    fun mayLaunchUrl(vararg uris: Uri) = browserLauncher.mayLaunchUrl(*uris)

    override fun onCleared() {
        browserLauncher.shutdown()
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY]!!
                val licenseInfoRepository = LicenseInfoRepository(
                    appContext = application,
                    mainCoroutineDispatcher = Dispatchers.Main,
                    ioCoroutineDispatcher = Dispatchers.IO
                )
                val browserLauncher = BrowserLauncher(application, application.packageManager)
                OpenSourceLicensesViewModel(
                    licenseInfoRepository,
                    browserLauncher
                )
            }
        }
    }
}
