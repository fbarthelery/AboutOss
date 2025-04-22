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

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.geekorum.aboutoss.sampleapp.ui.theme.AboutOssTheme
import com.geekorum.aboutoss.sampleapp.ui.theme.OpenSourceLicenseTheme
import com.geekorum.aboutoss.ui.material3.OpenSourceLicensesActivity
import com.geekorum.aboutoss.ui.material.OpenSourceLicensesActivity as Material2OpenSourceLicensesActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AboutOssTheme {
                Sample(
                    onMaterial2Click = {
                        startMaterial2LicenseActivity()
                    },
                    onMaterial3Click = {
                        startMaterial3LicenseActivity()
                    }
                )
            }
        }
    }

    private fun startMaterial2LicenseActivity() {
        val intent = if (BuildConfig.DEBUG) {
            // we launch a custom activity in debug to display some fake licenses
            // see Material2AcPrebuiltLicencesMaterial2Activitytivity for more info
            Intent(this, PrebuiltLicencesMaterial2Activity::class.java)
        } else {
            Intent(this, Material2OpenSourceLicensesActivity::class.java)
        }
        startActivity(intent)
    }

    private fun startMaterial3LicenseActivity() {
        // Don't use default MaterialTheme but supply our own
        OpenSourceLicensesActivity.themeProvider = { content ->
            OpenSourceLicenseTheme(content)
        }
        val intent = if (BuildConfig.DEBUG) {
            // we launch a custom activity in debug to display some fake licenses
            // see PrebuiltLicencesMaterial3Activity for more info
            Intent(this, PrebuiltLicencesMaterial3Activity::class.java)
        } else {
            Intent(this, OpenSourceLicensesActivity::class.java)
        }
        startActivity(intent)
    }
}

@Composable
private fun Sample(
    onMaterial2Click: () -> Unit,
    onMaterial3Click: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(Modifier.fillMaxSize()) {
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
        Text("This section launch a new activity to display licences information")
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


@Preview(showBackground = true)
@Composable
fun LauncherActivitySectionPreview() {
    AboutOssTheme {
        LaunchActivitySection(onMaterial2Click = {}, onMaterial3Click = {})
    }
}