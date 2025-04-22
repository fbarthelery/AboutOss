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

import android.content.Context
import androidx.core.net.toUri
import com.geekorum.geekdroid.network.BrowserLauncher as GeekdroidBrowserLauncher

class AndroidBrowserLauncher(
    private val context: Context,
    private val delegate: GeekdroidBrowserLauncher
) : BrowserLauncher {
    override fun warmUp() {
        delegate.warmUp(null)
    }

    override fun launchUrl(link: String) {
        delegate.launchUrl(context, link.toUri(), null as GeekdroidBrowserLauncher.LaunchCustomizer?)
    }

    override fun mayLaunchUrl(vararg uris: String) {
        val asUris = uris.map { it.toUri() }.toTypedArray()
        delegate.mayLaunchUrl(*asUris)
    }

    override fun shutdown() {
        delegate.shutdown()
    }
}