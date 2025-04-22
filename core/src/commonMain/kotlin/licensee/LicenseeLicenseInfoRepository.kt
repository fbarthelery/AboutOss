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
package com.geekorum.aboutoss.core.licensee

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import okio.Source

class LicenseeLicenseInfoRepository(
    private val produceInput: suspend () -> Source,
    private val mainCoroutineDispatcher: CoroutineDispatcher,
    private val ioCoroutineDispatcher: CoroutineDispatcher,
) {

    private var licensesInfo: Map<String, String>? = null

    suspend fun getLicensesInfo(): Map<String, String> = withContext(mainCoroutineDispatcher) {
        parseLicenses()
        checkNotNull(licensesInfo)
    }

    suspend fun getLicenseFor(dependency: String): String = withContext(mainCoroutineDispatcher) {
        parseLicenses()
        checkNotNull(licensesInfo).let {
            return@withContext it[dependency] ?: error("Dependency not found")
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private suspend fun parseLicenses() = withContext(mainCoroutineDispatcher) {
        if (licensesInfo == null) {
            val licenses = withContext(ioCoroutineDispatcher) {
                LicenseeParser(produceInput()).use {
                    it.readLicensee()
                }
            }
            licensesInfo = licenses
        }
    }
}

