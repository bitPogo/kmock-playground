/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kmock.example.dependency

object Dependency {
    val gradle = GradlePlugin
    val antiBytes = AntiBytes

    object AntiBytes {
        val gradle = Gradle

        object Gradle {
            const val util = "tech.antibytes.gradle-plugins:antibytes-gradle-utils:${Version.gradle.antibytes}"
            val kmock = "tech.antibytes.kmock-gradle:kmock:${Version.antibytes.kmock}"
        }

        val test = Test

        object Test {
            val annotations = "tech.antibytes.test-utils-kmp:test-utils-annotations:${Version.antibytes.test}"
            val core = "tech.antibytes.test-utils-kmp:test-utils:${Version.antibytes.test}"
            val fixture = "tech.antibytes.test-utils-kmp:test-utils-fixture:${Version.antibytes.test}"
            val coroutine = "tech.antibytes.test-utils-kmp:test-utils-coroutine:${Version.antibytes.test}"
            val ktor = "tech.antibytes.test-utils-kmp:test-utils-ktor:${Version.antibytes.test}"
            val gradle = "tech.antibytes.gradle-plugins:antibytes-gradle-test-utils:${Version.gradle.antibytes}"
            val kmock = "tech.antibytes.kmock:kmock:${Version.antibytes.kmock}"
            val kmockProcessor = "tech.antibytes.kmock:kmock-processor:${Version.antibytes.kmock}"
        }
    }
}
