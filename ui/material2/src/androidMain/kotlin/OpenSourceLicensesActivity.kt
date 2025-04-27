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
package com.geekorum.aboutoss.ui.material

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.geekorum.aboutoss.core.gms.GmsLicenseInfoRepository
import com.geekorum.aboutoss.ui.common.BaseOpensourceLicenseActivity
import com.geekorum.aboutoss.ui.common.Factory
import com.geekorum.aboutoss.ui.common.OpenSourceLicensesViewModel
import com.geekorum.aboutoss.ui.material.OpenSourceLicensesActivity.Companion.themeProvider
import kotlinx.coroutines.Dispatchers

/**
 * Activity to display opensource license information
 *
 * This activity use Material compose to create the UI.
 * You can specify the Material theme to use by setting [themeProvider]
 * before launching the activity
 */
open class OpenSourceLicensesActivity : BaseOpensourceLicenseActivity() {
    override val viewModel: OpenSourceLicensesViewModel by viewModels(
        factoryProducer = {
            val gmsLicenseInfoRepository = GmsLicenseInfoRepository(
                appContext = applicationContext,
                mainCoroutineDispatcher = Dispatchers.Main,
                ioCoroutineDispatcher = Dispatchers.IO,
            )
            OpenSourceLicensesViewModel.Factory(gmsLicenseInfoRepository)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            themeProvider {
                OpenSourceDependenciesNavHost(
                    openSourceLicensesViewModel = viewModel,
                    navigateUp = {
                        if (!onNavigateUp()) {
                            finish()
                        }
                    }
                )
            }
        }
    }

    companion object {
        /**
         * The composable Theme function to set the theme of the UI in [OpenSourceLicensesActivity]
         * Default to base material theme [MaterialTheme]
         */
        var themeProvider: @Composable (@Composable () -> Unit) -> Unit = { content ->
            val colors = if (isSystemInDarkTheme()) darkColors() else lightColors()
            MaterialTheme(colors = colors, content = content)
        }
    }
}

