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

import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.MavenPublishPlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure

internal fun Project.configureMavenPublish() {
    apply<MavenPublishPlugin>()

    configure<MavenPublishBaseExtension> {
        publishToMavenCentral()
        signAllPublications()

        // default pom info. each field can be overridden in specific project
        pom {
            name = "${project.group}:${project.name}"
            description = "A library to retrieve and display opensource licenses in applications"
            inceptionYear = "2023"
            val githubUrl = "https://github.com/fbarthelery/AboutOss/"
            url = githubUrl
            scm {
                url = githubUrl
                connection = "scm:git:$githubUrl.git"
            }

            licenses {
                license {
                    name = "GPL-3.0-or-later"
                    url = "https://www.gnu.org/licenses/gpl-3.0.html"
                    distribution = "repo"
                }
            }

            developers {
                developer {
                    id = "da_risk"
                    name = "Frédéric Barthéléry"
                    email = "da_risk@geekorum.com"
                }
            }
        }
    }
}