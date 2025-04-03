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
package com.geekorum.aboutoss.core.licensee

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okio.Source
import okio.buffer

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


private class LicenseeParser(
    input: Source
): AutoCloseable {
    private val buffered = input.buffer()

    fun readLicensee(): Map<String, String> {
        val json = Json {
            ignoreUnknownKeys = true
        }
        val items: List<LicenseItem> = json.decodeFromString(buffered.readUtf8())

        return items.associate {
            val name = it.name ?: "${it.groupId}:${it.artifactId}"
            val license = it.spdxLicenses.firstNotNullOfOrNull {
                "${it.name}\n\n${it.url}"
            } ?: it.unknownLicenses.firstNotNullOf {
                "${it.name}\n\n${it.url}"
            }
            name to license
        }
    }

    override fun close() {
        buffered.close()
    }
}


@Serializable
private data class LicenseItem(
    val groupId: String,
    val artifactId: String,
    val version: String,
    val spdxLicenses: List<SpdxLicense> = emptyList(),
    val unknownLicenses: List<UnknownLicense> = emptyList(),
    val name: String? = null,
    val scm: Scm? = null,
)

@Serializable
private data class SpdxLicense(
    val identifier: String,
    val name: String,
    val url: String,
)

@Serializable
private data class UnknownLicense(
    val name: String,
    val url: String
)

@Serializable
private data class Scm(
    val url: String,
)