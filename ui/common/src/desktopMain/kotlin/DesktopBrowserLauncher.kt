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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.awt.Desktop
import java.net.URI
import java.util.Locale

/**
 * A [BrowserLauncher] for the desktop platform
 */
class DesktopBrowserLauncher : BrowserLauncher {

    private val desktopLauncher = run {
        if (Desktop.isDesktopSupported()) {
            val desktop = Desktop.getDesktop()
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                return@run DesktopLauncher(desktop)
            }
        }
        null
    }

    private val openCommandLauncher = OpenCommandLauncher()

    override fun launchUrl(link: String) {
        try {
            desktopLauncher?.launchUrl(link)
            return
        } catch (e: Exception) {
            e.printStackTrace()
            System.err.println("Unable to launch url $link. Use fallback")
        }

        try {
            openCommandLauncher.launchUrl(link)
            return
        } catch (e: Exception) {
            e.printStackTrace()
            System.err.println("Unable to launch url $link")
        }
    }

    override fun mayLaunchUrl(vararg uris: String) {
    }

}

/**
 * Creates and [androidx.compose.runtime.remember] a [BrowserLauncher]
 */
@Composable
actual fun rememberBrowserLauncher(): BrowserLauncher {
    return remember { DesktopBrowserLauncher() }
}

private interface Launcher {
    fun launchUrl(link: String)
}

private class DesktopLauncher(
    private val desktop: Desktop
) : Launcher {
    override fun launchUrl(link: String) {
        desktop.browse(URI(link))
    }
}

private class OpenCommandLauncher: Launcher {
    override fun launchUrl(link: String) {
        val osName = System.getProperty("os.name").lowercase(Locale.getDefault())
        val command = when {
            "mac" in osName -> "open"
            "nix" in osName || "nux" in osName -> "xdg-open"
            else -> error("cannot open $link")
        }

        ProcessBuilder(command, link)
            .start()
    }
}