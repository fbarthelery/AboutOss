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
package com.geekorum.aboutoss.ui.material3

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.geekorum.aboutoss.ui.common.OpenSourceLicensesViewModel
import com.geekorum.aboutoss.ui.common.R as commonR

/**
 * Display the list of dependencies used in the application
 *
 * @param viewModel the [OpenSourceLicensesViewModel] to use
 * @param onDependencyClick lambda to execute on click on one dependency item
 * @param onUpClick lambda to execute on click on the up arrow
 */
@Composable
fun OpenSourceDependenciesListScreen(
    viewModel: OpenSourceLicensesViewModel,
    onDependencyClick: (String) -> Unit,
    onUpClick: () -> Unit
) {
    val dependencies by viewModel.dependenciesList.collectAsState(initial = emptyList())
    OpenSourceDependenciesListScreen(
        dependencies = dependencies,
        onDependencyClick = onDependencyClick,
        onUpClick = onUpClick
    )
}

/**
 * Display the list of dependencies used in the application
 *
 * @param dependencies the list of dependencies
 * @param onDependencyClick lambda to execute on click on one dependency item
 * @param onUpClick lambda to execute on click on the up arrow
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenSourceDependenciesListScreen(
    dependencies: List<String>,
    onDependencyClick: (String) -> Unit,
    onUpClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(commonR.string.title_oss_licenses)) },
                navigationIcon = {
                    IconButton(onClick = onUpClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }) {
        LazyColumn(Modifier.fillMaxSize(), contentPadding = it) {
            items(dependencies) {
                Column {
                    ListItem(
                        modifier = Modifier
                            .height(64.dp)
                            .clickable(onClick = { onDependencyClick(it) }),
                        headlineContent = {
                            Text(
                                it, modifier = Modifier.padding(horizontal = 16.dp),
                                overflow = TextOverflow.Ellipsis, maxLines = 1
                            )
                        }
                    )
                    Divider(Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}
