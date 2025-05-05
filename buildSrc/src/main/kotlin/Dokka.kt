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

import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.DokkaPlugin


internal fun Project.configureDokka() {
    apply<DokkaPlugin>()

    configure<DokkaExtension> {
        dokkaSourceSets.configureEach {
            reportUndocumented = true
            sourceLink {
                localDirectory = rootDir
                remoteUrl = uri("https://github.com/fbarthelery/AboutOss/tree/main/")
            }

            externalDocumentationLinks.register("okio") {
                url = uri("https://square.github.io/okio/3.x/okio/okio/")
            }
            externalDocumentationLinks.register("kotlinx-coroutines") {
                url = uri("https://kotlinlang.org/api/kotlinx.coroutines/")
            }

            perPackageOption {
                matchingRegex = ".*\\.generated\\.resources"
                suppress = true
            }
        }
    }

}