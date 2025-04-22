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
package com.geekorum.aboutoss.core.gms

import android.annotation.SuppressLint
import android.content.Context
import java.io.InputStream

@SuppressLint("DiscouragedApi")
fun OssLicenseParser.Companion.openDefaultThirdPartyLicenses(context: Context): InputStream {
    return openRawResourcesByName(context, "third_party_licenses")
}

@SuppressLint("DiscouragedApi")
fun OssLicenseParser.Companion.openDefaultThirdPartyLicensesMetadata(context: Context): InputStream {
    return openRawResourcesByName(context, "third_party_license_metadata")
}

@SuppressLint("DiscouragedApi")
fun OssLicenseParser.Companion.openRawResourcesByName(context: Context, name: String): InputStream {
    val resourceId = context.resources.getIdentifier(name, "raw", context.packageName)
    check(resourceId != 0) { "$name was not found in resources raw of ${context.packageName}"}
    return context.resources.openRawResource(resourceId)
}
