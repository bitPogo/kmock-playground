/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kmock.example.dependency

object GradlePlugin {
    const val dependency = "tech.antibytes.gradle-plugins:antibytes-dependency:${Version.Gradle.antibytes}"
    const val projectConfig = "tech.antibytes.gradle-plugins:antibytes-configuration:${Version.Gradle.antibytes}"
    const val spotless = "com.diffplug.spotless:spotless-plugin-gradle:${Version.Gradle.spotless}"
    const val kmock = "tech.antibytes.kmock:kmock-gradle:${Version.antibytes.kmock}"
}
