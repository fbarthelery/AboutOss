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
import kotlinx.coroutines.Dispatchers
import okio.Buffer

/**
 * Create a [com.geekorum.aboutoss.core.licensee.LicenseeLicenseInfoRepository]
 */
fun LicenseeLicenseInfoRepository(
    licenseeResourcePath: String = "app/cash/licensee/artifacts.json",
    mainCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Main,
    ioCoroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
): LicenseeLicenseInfoRepository {
    return LicenseeLicenseInfoRepository(
        produceInput = {
            val buffer = Buffer()
            buffer.write(readResources(licenseeResourcePath))
            buffer
        },
        mainCoroutineDispatcher = mainCoroutineDispatcher,
        ioCoroutineDispatcher = ioCoroutineDispatcher
    )
}


private fun readResources(path: String): ByteArray {
    val classLoader = LicenseeLicenseInfoRepository::class.java.classLoader
    val resource = classLoader.getResourceAsStream(path)
    return resource.use { input -> input.readBytes() }
}

