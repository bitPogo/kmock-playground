/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kmock.example.dependency

object Version {

    val gradle = Gradle

    object Gradle {
        /**
         * [AnitBytes GradlePlugins](https://github.com/bitPogo/gradle-plugins)
         */
        const val antibytes = "bb22160"

        /**
         * [Spotless](https://plugins.gradle.org/plugin/com.diffplug.gradle.spotless)
         */
        const val spotless = "6.3.0"
    }

    val antibytes = Antibytes

    object Antibytes {
        val test = "8b81c5a"

        /**
         * [KMock](https://github.com/bitPogo/kmock)
         */
        const val kmock = "70b940a-fix-single-sources-SNAPSHOT"
    }
}
