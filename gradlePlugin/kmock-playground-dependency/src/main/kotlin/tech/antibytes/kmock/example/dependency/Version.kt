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
        const val antibytes = "b1a580e"

        /**
         * [Spotless](https://plugins.gradle.org/plugin/com.diffplug.gradle.spotless)
         */
        const val spotless = "6.4.2"
    }

    val antibytes = Antibytes

    object Antibytes {
        const val test = "98f5a1d"

        /**
         * [KMock](https://github.com/bitPogo/kmock)
         */
        const val kmock = "0.2.1"

        /**
         * [KFixture](https://github.com/bitPogo/kfixture)
         */
        const val kfixture = "0.1.0"
    }
}
