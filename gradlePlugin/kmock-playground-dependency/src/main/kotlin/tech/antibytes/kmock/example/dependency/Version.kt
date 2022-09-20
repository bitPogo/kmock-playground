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
        const val antibytes = "d40a207"

        /**
         * [Spotless](https://plugins.gradle.org/plugin/com.diffplug.gradle.spotless)
         */
        const val spotless = "6.11.0"
    }

    val antibytes = Antibytes

    object Antibytes {
        const val test = "8c5f468"

        /**
         * [KMock](https://github.com/bitPogo/kmock)
         */
        const val kmock = "0.3.0-rc04"

        /**
         * [KFixture](https://github.com/bitPogo/kfixture)
         */
        const val kfixture = "0.3.1"
    }
}
