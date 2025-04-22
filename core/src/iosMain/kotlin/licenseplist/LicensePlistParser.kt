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
package com.geekorum.aboutoss.core.licenseplist

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSPropertyListMutableContainers
import platform.Foundation.NSPropertyListSerialization
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LicensePlistParser {

    @Suppress("UNCHECKED_CAST")
    suspend fun parseLicenses(
        licensePlistInput: NSURL
    ): Map<String, String> {
        val fileContent = getContent(licensePlistInput)
        val plist = fileContent.toPropertyList() as Map<String, List<Map<String, String>>>
        val paneLists = plist["PreferenceSpecifiers"]!!
            .filter {
                it["Type"] == "PSChildPaneSpecifier"
            }
        val directoryUrl = licensePlistInput.URLByDeletingLastPathComponent()!!
        return paneLists.associate { pane ->
            val paneFile = pane["File"]!!
            val libraryName = pane["Title"]!!
            val paneUrl = buildPaneUrl(directoryUrl, paneFile)
            val paneFileContent =  getContent(paneUrl)
            val paneFilePlist = paneFileContent.toPropertyList() as Map<String, List<Map<String, String>>>
            val license = getLicenseFromPaneFilePlist(paneFilePlist)
            libraryName to license
        }
    }

    private fun buildPaneUrl(directoryUrl: NSURL, paneName: String) = directoryUrl.URLByAppendingPathComponent(paneName)!!.URLByAppendingPathExtension("plist")!!

    private fun getLicenseFromPaneFilePlist(paneFilePList: Map<String, List<Map<String, String>>>): String {
        val specifiers = paneFilePList["PreferenceSpecifiers"]!!
        val licenses = specifiers.map {
            it["FooterText"]!!
        }
        return licenses.joinToString("\n\n")
    }

    private fun getContent(url: NSURL): NSData {
        return checkNotNull(NSData.dataWithContentsOfURL(url))
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    private suspend fun NSData.toPropertyList(): Any? = suspendCoroutine { cont ->
        val parsed = memScoped {
            val error: ObjCObjectVar<NSError?> = alloc()
            val result = NSPropertyListSerialization.propertyListWithData(this@toPropertyList,
                options = NSPropertyListMutableContainers,
                format = null,
                error.ptr
            )
            if (error.value != null) {
                cont.resumeWithException(Exception(error.value!!.description))
            }
            result
        }
        cont.resume(parsed!!)
    }

    companion object {
        fun getDefaultLicensePlistUrl(): NSURL {
            val path = NSBundle.mainBundle.pathForResource(
                "com.mono0926.LicensePlist",
                ofType = "plist",
                inDirectory = "licenseplist"
            )
            return NSURL.fileURLWithPath(path!!)
        }
    }
}