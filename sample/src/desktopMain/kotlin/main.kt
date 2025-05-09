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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun SampleAppDesktop() {
    var material2IsOpen by remember { mutableStateOf(false) }
    var material3IsOpen by remember { mutableStateOf(false) }
    SampleApp(
        onMaterial2Click = {
            material2IsOpen = true
        },
        onMaterial3Click = {
            material3IsOpen = true
        })

    if (material2IsOpen) {
        PrebuiltLicencesMaterial2Window(onCloseRequest = {
            material2IsOpen = false
        })
    }
    if (material3IsOpen) {
        PrebuiltLicencesMaterial3Window(onCloseRequest = {
            material3IsOpen = false
        })
    }

}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        SampleAppDesktop()
    }
}
