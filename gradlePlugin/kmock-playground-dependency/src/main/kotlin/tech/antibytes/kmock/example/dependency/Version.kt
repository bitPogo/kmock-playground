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
        const val antibytes = "18bb640"

        /**
         * [Spotless](https://plugins.gradle.org/plugin/com.diffplug.gradle.spotless)
         */
        const val spotless = "6.4.2"
    }

    val antibytes = Antibytes

    object Antibytes {
        const val test = "f25b461"

        /**
         * [KMock](https://github.com/bitPogo/kmock)
         */
        const val kmock = "0.3.0-rc01"

        /**
         * [KFixture](https://github.com/bitPogo/kfixture)
         */
        const val kfixture = "0.2.0"
    }
}
