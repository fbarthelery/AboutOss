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

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.geekorum.aboutoss.ui.common.BrowserLauncher
import com.geekorum.aboutoss.ui.common.OpenSourceLicensesViewModel
import com.geekorum.aboutoss.ui.common.rememberBrowserLauncher

/**
 * Display the opensource license of a dependency
 *
 * @param viewModel the [OpenSourceLicensesViewModel] to use
 * @param dependency the dependency
 * @param onUpClick lambda to execute on click on the up arrow
 */
@Composable
fun OpenSourceLicenseScreen(
    viewModel: OpenSourceLicensesViewModel,
    dependency: String,
    onUpClick: () -> Unit,
    browserLauncher: BrowserLauncher = rememberBrowserLauncher()
) {
    val license by viewModel.getLicenseDependency(dependency).collectAsStateWithLifecycle("")
    OpenSourceLicenseScreen(
        dependency = dependency,
        license = license,
        onUpClick = onUpClick,
        onUrlClick = {
            browserLauncher.launchUrl(it)
        },
        onUrlsFound = {
            browserLauncher.mayLaunchUrl(*it.toTypedArray())
        }
    )
}

/**
 * Display the opensource license of a dependency
 *
 * @param dependency the dependency
 * @param license the opensource license text
 * @param onUpClick lambda to execute on click on the up arrow
 * @param onUrlClick lambda to execute on click on a url
 * @param onUrlsFound lambda to execute when all urls in the license have been found
 */
@OptIn(ExperimentalLayoutApi::class, ExperimentalTextApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OpenSourceLicenseScreen(
    dependency: String,
    license: String,
    onUpClick: () -> Unit,
    onUrlClick: (String) -> Unit,
    onUrlsFound: (List<String>) -> Unit,
) {
    val linkifiedLicense = linkifyText(text = license, onUrlClick = onUrlClick)
    LaunchedEffect(linkifiedLicense) {
        val uris =
            linkifiedLicense.getLinkAnnotations(0, linkifiedLicense.length).map { it.item }
                .filterIsInstance<LinkAnnotation.Url>()
                .map { it.url }
        onUrlsFound(uris)
    }

    val scrollState = rememberScrollState()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
        TopAppBar(title = { Text(dependency, overflow = TextOverflow.Ellipsis, maxLines = 1) },
            navigationIcon = {
                IconButton(onClick = onUpClick) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            },
        )
    }) { paddingValues ->
        Text(linkifiedLicense,
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState)
        )
    }
}

/**
 * https://regexr.com/37i6s
 */
private val urlRegexp = """https?://(www\.)?[-a-zA-Z0-9@:%._+~#=]{2,256}\.[a-z]{2,4}\b([-a-zA-Z0-9@:%_+.~#?&/=]*)""".toRegex()

@OptIn(ExperimentalTextApi::class)
@Composable
internal fun linkifyText(text: String, onUrlClick: (String) -> Unit): AnnotatedString {
    val style = SpanStyle(
        color = MaterialTheme.colorScheme.primary,
        textDecoration = TextDecoration.Underline
    )
    return remember(text, style) {
        buildAnnotatedString {
            var currentIdx = 0
            for (match in urlRegexp.findAll(text)) {
                if (currentIdx < match.range.first) {
                    append(text.substring(currentIdx, match.range.first))
                }
                val url = text.substring(match.range)
                val linkInteractionListener = LinkInteractionListener {
                    val url = when(it) {
                        is LinkAnnotation.Url -> it.url
                        is LinkAnnotation.Clickable -> it.tag
                        else -> null
                    }
                    if (url != null) {
                        onUrlClick(url)
                    }
                }
                withLink(LinkAnnotation.Url(url, linkInteractionListener = linkInteractionListener)) {
                    withStyle(style) {
                        append(url)
                    }
                }
                currentIdx = match.range.last + 1
            }
            append(text.substring(currentIdx))
        }
    }
}
