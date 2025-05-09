/*
 * AboutOss is a utility library to retrieve and display
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
package com.geekorum.build

import org.gradle.api.artifacts.repositories.MavenArtifactRepository

/**
 * Setup the content of google() repository
 */
fun MavenArtifactRepository.setupGoogleContent() = apply {
    require(name == "Google") { "Only apply to `google()` repository "}
    content {
        includeGroupByRegex("""android\.arch\..*""")
        includeGroupByRegex("""androidx\..*""")
        includeGroupByRegex("""com\.android\..*""")
        includeGroupByRegex("""com\.google\..*""")
        includeGroup("com.crashlytics.sdk.android")
        includeGroup("io.fabric.sdk.android")
        includeGroup("org.chromium.net")
        includeGroup("zipflinger")
        includeGroup("com.android")
    }
}
