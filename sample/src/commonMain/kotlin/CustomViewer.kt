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

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.geekorum.aboutoss.ui.common.OpenSourceLicensesViewModel

@Composable
fun CustomViewer(
    viewModel: OpenSourceLicensesViewModel = viewModel(
        initializer = {
            createPrebuildOpenSourceLicensesViewModel()
        }
    ),
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text("This section shows our you can use a custom ui to display licenses")
        DependenciesGrid(viewModel, Modifier.padding(top = 16.dp))
    }
}

@Composable
private fun DependenciesGrid(
    viewModel: OpenSourceLicensesViewModel,
    modifier: Modifier = Modifier
) {
    val dependencies by viewModel.dependenciesList.collectAsStateWithLifecycle(emptyList())
    var selected by remember { mutableStateOf(-1) }
    LazyVerticalGrid(
        GridCells.Adaptive(150.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        itemsIndexed(dependencies) { idx, dependency ->
            if (idx == selected) {
                val license by viewModel.getLicenseDependency(dependency)
                    .collectAsStateWithLifecycle("")
                LicenseCard(license, onClick = {
                    selected = -1
                })
            } else {
                DependencyCard(dependency, onClick = {
                    selected = idx
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LicenseCard(license: String, onClick: () -> Unit) {
    Card(modifier = Modifier.size(150.dp), onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Text(
            license, style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .wrapContentSize(
                    Alignment.Center
                )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DependencyCard(dependency: String, onClick: () -> Unit) {
    Card(modifier = Modifier.size(150.dp), onClick = onClick) {
        Text(
            dependency,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .wrapContentSize(
                    Alignment.Center
                )
        )
    }
}
