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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.geekorum.aboutoss.sampleapp.ui.theme.AboutOssTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SampleApp(
    onMaterial2Click: () -> Unit,
    onMaterial3Click: () -> Unit,
) {
    Scaffold {
        Column(Modifier.fillMaxSize().padding(it)) {
            LaunchActivitySection(onMaterial2Click, onMaterial3Click)
            CustomViewer(modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}

@Composable
private fun LaunchActivitySection(
    onMaterial2Click: () -> Unit,
    onMaterial3Click: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.padding(16.dp)) {
        Text(text = "This section launch a new activity to display licences information")
        Row(
            horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            Material2Card(onClick = onMaterial2Click)
            Material3Card(onClick = onMaterial3Click)
        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Material2Card(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier, onClick = onClick) {
        Column(Modifier.padding(16.dp)) {
            Text("Material2 UI", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Material3Card(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier, onClick = onClick) {
        Column(Modifier.padding(16.dp)) {
            Text("Material3 UI", style = MaterialTheme.typography.labelLarge)
        }
    }
}


@Preview
@Composable
fun LauncherActivitySectionPreview() {
    AboutOssTheme {
        Surface {
            LaunchActivitySection(onMaterial2Click = {}, onMaterial3Click = {})
        }
    }
}