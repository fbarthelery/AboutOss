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

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.geekorum.aboutoss.ui.common.OpenSourceLicensesViewModel

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
) {
    val context = LocalContext.current
    val license by viewModel.getLicenseDependency(dependency).collectAsState("")
    OpenSourceLicenseScreen(
        dependency = dependency,
        license = license,
        onUpClick = onUpClick,
        onUrlClick = {
            viewModel.openLinkInBrowser(context, it)
        },
        onUrlsFound = {
            val uris = it.map { uri -> uri.toUri() }
            viewModel.mayLaunchUrl(*uris.toTypedArray())
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
    val linkifiedLicense = linkifyText(text = license)
    LaunchedEffect(linkifiedLicense) {
        val uris =
            linkifiedLicense.getUrlAnnotations(0, linkifiedLicense.length).map { it.item.url }
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
                        Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            },
        )
    }) { paddingValues ->
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

        Text(linkifiedLicense,
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

/**
 * https://regexr.com/37i6s
 */
private val urlRegexp = """https?://(www\.)?[-a-zA-Z0-9@:%._+~#=]{2,256}\.[a-z]{2,4}\b([-a-zA-Z0-9@:%_+.~#?&/=]*)""".toRegex()

@OptIn(ExperimentalTextApi::class)
@Composable
private fun linkifyText(text: String): AnnotatedString {
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
                withAnnotation(UrlAnnotation(url)) {
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
