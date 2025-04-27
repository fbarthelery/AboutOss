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
package com.geekorum.aboutoss.ui.material3

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldValue
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.geekorum.aboutoss.common.generated.resources.title_oss_licenses
import com.geekorum.aboutoss.ui.common.OpenSourceLicensesViewModel
import com.geekorum.aboutoss.ui.common.rememberBrowserLauncher
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.geekorum.aboutoss.common.generated.resources.Res as CommonRes


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AdaptiveOpenSourceDependenciesScreen(
    openSourceLicensesViewModel: OpenSourceLicensesViewModel,
    navigateUp: () -> Unit
) {
    val dependencies by openSourceLicensesViewModel.dependenciesList.collectAsStateWithLifecycle(emptyList())
    val browserLauncher = rememberBrowserLauncher()
    val coroutineScope = rememberCoroutineScope()
    val onUrlsFound: (List<String>) -> Unit = {
        browserLauncher.mayLaunchUrl(*it.toTypedArray())
    }
    val onUrlClick: (String) -> Unit = {
        browserLauncher.launchUrl(it)
    }

    AdaptiveOpenSourceDependenciesScreen(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        dependenciesListPane = {
            AdaptiveOpenSourceDependenciesListPane(
                isSinglePane = isSinglePane,
                dependencies = dependencies,
                selectedDependency = selectedDependency,
                onDependencyClick = {
                    coroutineScope.launch {
                        showLicenseDetails(it)
                    }
                },
                onUpClick = navigateUp,
            )
        },
        dependencyLicensePane = {dependency ->
            if (dependency != null) {
                val license by openSourceLicensesViewModel.getLicenseDependency(dependency).collectAsStateWithLifecycle("")
                AdaptiveOpenSourceLicensePane(
                    isSinglePane = isSinglePane,
                    dependency = dependency,
                    license = license,
                    onUpClick = {
                        coroutineScope.launch {
                            navigateBack()
                        }
                    },
                    onUrlClick = onUrlClick,
                    onUrlsFound = onUrlsFound,
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalComposeUiApi::class)
@Composable
fun AdaptiveOpenSourceDependenciesScreen(
    dependenciesListPane: @Composable OpenSourcePaneScope.() -> Unit,
    dependencyLicensePane: @Composable OpenSourcePaneScope.(dependency: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<String>()
    val coroutineScope = rememberCoroutineScope()
    BackHandler(navigator.canNavigateBack()) {
        coroutineScope.launch {
            navigator.navigateBack()
        }
    }

    val scope = remember(navigator) { DefaultOpenSourcePaneScope(navigator)  }
    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        scaffoldState = navigator.scaffoldState,
        listPane = {
            AnimatedPane {
                scope.dependenciesListPane()
            }
        },
        detailPane = {
            AnimatedPane {
                val dependency = navigator.currentDestination?.contentKey
                scope.dependencyLicensePane(dependency)
            }
        }
    )
}


/**
 * Display the list of dependencies used in the application
 *
 * @param isSinglePane if only a single pane is visible
 * @param dependencies the list of dependencies
 * @param selectedDependency the currently selected dependency
 * @param onDependencyClick lambda to execute on click on one dependency item
 * @param onUpClick lambda to execute on click on the up arrow
 */
@Composable
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun AdaptiveOpenSourceDependenciesListPane(
    isSinglePane: Boolean,
    dependencies: List<String>,
    selectedDependency: String?,
    onDependencyClick: (String) -> Unit,
    onUpClick: () -> Unit,
) {
    if (isSinglePane) {
        OpenSourceDependenciesListScreen(
            dependencies,
            onDependencyClick = onDependencyClick,
            onUpClick = onUpClick
        )
    } else {
        OpenSourceDependenciesListPane(
            dependencies,
            selectedDependency = selectedDependency,
            onDependencyClick = onDependencyClick,
            onUpClick = onUpClick
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OpenSourceDependenciesListPane(
    dependencies: List<String>,
    selectedDependency: String?,
    onDependencyClick: (String) -> Unit,
    onUpClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(CommonRes.string.title_oss_licenses)) },
                navigationIcon = {
                    IconButton(onClick = onUpClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
            )
        }
    ) {
        LazyColumn(Modifier.fillMaxSize(), contentPadding = it) {
            items(dependencies) { dependency ->
                Column {
                    val colors = if (selectedDependency == dependency) {
                        ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            headlineColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            leadingIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            overlineColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            supportingColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            trailingIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            disabledHeadlineColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            disabledLeadingIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            disabledTrailingIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                    } else {
                        ListItemDefaults.colors()
                    }
                    ListItem(
                        modifier = Modifier.clickable(onClick = { onDependencyClick(dependency) }),
                        colors = colors,
                        headlineContent = {
                            Text(dependency, overflow = TextOverflow.Ellipsis, maxLines = 1)
                        }
                    )
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}


/**
 * Display the opensource license of a dependency
 *
 * @param isSinglePane if only a single pane is visible
 * @param dependency the dependency
 * @param license the opensource license text
 * @param onUpClick lambda to execute on click on the navigate up button
 * @param onUrlClick lambda to execute on click on a url
 * @param onUrlsFound lambda to execute when all urls in the license have been found
 */
@Composable
fun AdaptiveOpenSourceLicensePane(
    isSinglePane: Boolean,
    dependency: String,
    license: String,
    onUpClick: () -> Unit,
    onUrlClick: (String) -> Unit,
    onUrlsFound: (List<String>) -> Unit,
) {
    if (isSinglePane) {
        OpenSourceLicenseScreen(
            dependency = dependency,
            license = license,
            onUpClick = onUpClick,
            onUrlsFound = onUrlsFound,
            onUrlClick = onUrlClick
        )
    } else {
        OpenSourceLicensePane(
            dependency = dependency,
            license = license,
            onUrlsFound = onUrlsFound,
            onUrlClick = onUrlClick
        )
    }
}

/**
 * Display the opensource license of a dependency
 *
 * @param dependency the dependency
 * @param license the opensource license text
 * @param onUrlClick lambda to execute on click on a url
 * @param onUrlsFound lambda to execute when all urls in the license have been found
 */
@OptIn(ExperimentalLayoutApi::class, ExperimentalTextApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun OpenSourceLicensePane(
    dependency: String,
    license: String,
    onUrlClick: (String) -> Unit,
    onUrlsFound: (List<String>) -> Unit,
) {
    val linkifiedLicense = linkifyText(text = license)
    LaunchedEffect(linkifiedLicense) {
        val uris =
            linkifiedLicense.getUrlAnnotations(0, linkifiedLicense.length).map { it.item.url }
        onUrlsFound(uris)
    }

    Surface(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Vertical))
            .padding(end = 24.dp)
    ) {
        val scrollState = rememberScrollState()
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    ),
                    scrollBehavior = scrollBehavior,
                    title = { Text(dependency, overflow = TextOverflow.Ellipsis, maxLines = 1) }
                )
            }
        ) { paddingValues ->
            val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
            val pressIndicator = Modifier.pointerInput(layoutResult, linkifiedLicense) {
                detectTapGestures { pos ->
                    layoutResult.value?.let { layoutResult ->
                        val posWithScroll = pos.copy(y = pos.y + scrollState.value)
                        val offset = layoutResult.getOffsetForPosition(posWithScroll)
                        linkifiedLicense.getUrlAnnotations(start = offset, end = offset)
                            .firstOrNull()?.let { annotation ->
                                onUrlClick(annotation.item.url)
                            }
                    }
                }
            }

            Text(
                linkifiedLicense,
                modifier = Modifier
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
                    .then(pressIndicator)
                    .verticalScroll(scrollState),
                onTextLayout = {
                    layoutResult.value = it
                }
            )
        }
    }
}

@Stable
interface OpenSourcePaneScope {
    val isSinglePane: Boolean
    val selectedDependency: String?

    suspend fun showLicenseDetails(dependency: String)

    suspend fun navigateBack()
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private class DefaultOpenSourcePaneScope(
    val navigator: ThreePaneScaffoldNavigator<String>,
) : OpenSourcePaneScope {
    override val isSinglePane: Boolean
        get() = navigator.scaffoldState.targetState.isSinglePane()

    override val selectedDependency: String?
        get() = navigator.currentDestination?.contentKey

    override suspend fun showLicenseDetails(dependency: String) {
        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, dependency)
    }

    override suspend fun navigateBack() {
        navigator.navigateBack()
    }

}


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun ThreePaneScaffoldValue.isSinglePane(): Boolean {
    return primary == PaneAdaptedValue.Expanded && secondary == PaneAdaptedValue.Hidden && tertiary == PaneAdaptedValue.Hidden
}


@Preview
@Composable
private fun PreviewOpenSourceLicensePane() {
    var singlePane by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxSize()) {
        AdaptiveOpenSourceLicensePane(
            singlePane,
            "Apache HttpCommons",
            "Apache 2.0",
            onUrlsFound = {},
            onUrlClick = {},
            onUpClick = {}
        )
        Button(modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
            onClick = {
            singlePane = !singlePane
        }) {
            Text("Toggle single pane")
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Preview
@Composable
private fun PreviewBrowsingPanes() {
    Surface {
        val dependencies = List(20) {
            "Dep $it"
        }
        val licenses = dependencies.associate { it to "license of $it" }
        val navigator = rememberListDetailPaneScaffoldNavigator<String>()
        val coroutineScope = rememberCoroutineScope()
        AdaptiveOpenSourceDependenciesScreen(
            dependenciesListPane = {
                OpenSourceDependenciesListPane(
                    dependencies,
                    selectedDependency = navigator.currentDestination?.contentKey,
                    onDependencyClick = {
                        coroutineScope.launch {
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, it)
                        }
                    },
                    onUpClick = {}
                )
            },
            dependencyLicensePane = { dependency ->
                if (dependency != null) {
                    OpenSourceLicensePane(
                        dependency,
                        licenses[dependency] ?: "",
                        onUrlClick = {},
                        onUrlsFound = {})
                }
            }
        )
    }
}