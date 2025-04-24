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
package com.geekorum.aboutoss.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geekorum.aboutoss.core.LicenseInfoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Manage opensource license information and allow to display them in an UI
 */
class OpenSourceLicensesViewModel(
    private val licenseInfoRepository: LicenseInfoRepository,
) : ViewModel() {

    private val licensesInfo = flow {
        emit(licenseInfoRepository.getLicensesInfo())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val dependenciesList = licensesInfo.map { licensesInfo ->
        licensesInfo.keys.sortedBy { it.lowercase() }
    }

    fun getLicenseDependency(dependency: String) = flow {
        emit(licenseInfoRepository.getLicenseFor(dependency))
    }

    companion object
}