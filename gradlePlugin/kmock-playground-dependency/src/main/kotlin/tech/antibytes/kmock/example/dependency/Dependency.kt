/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kmock.example.dependency

object Dependency {
    val gradle = GradlePlugin
    val antibytes = AntiBytes

    object AntiBytes {
        val gradle = Gradle

        object Gradle {
            const val kmock = "tech.antibytes.kmock-gradle:kmock:${Version.antibytes.kmock}"
        }

        val test = Test

        object Test {
            const val kmockProcessor = "tech.antibytes.kmock:kmock-processor:${Version.antibytes.kmock}"
            val kmp = KmpTest

            object KmpTest {
                const val annotations = "tech.antibytes.test-utils-kmp:test-utils-annotations:${Version.antibytes.test}"
                const val core = "tech.antibytes.test-utils-kmp:test-utils:${Version.antibytes.test}"
                const val fixture = "tech.antibytes.kfixture:core:${Version.antibytes.kfixture}"
                const val coroutine = "tech.antibytes.test-utils-kmp:test-utils-coroutine:${Version.antibytes.test}"
                const val kmock = "tech.antibytes.kmock:kmock:${Version.antibytes.kmock}"
            }

            val jvm = JvmTest

            object JvmTest {
                const val annotations = "tech.antibytes.test-utils-kmp:test-utils-annotations-jvm:${Version.antibytes.test}"
                const val core = "tech.antibytes.test-utils-kmp:test-utils-jvm:${Version.antibytes.test}"
                const val fixture = "tech.antibytes.kfixture:core-jvm:${Version.antibytes.kfixture}"
                const val coroutine = "tech.antibytes.test-utils-kmp:test-utils-coroutine-jvm:${Version.antibytes.test}"
                const val kmock = "tech.antibytes.kmock:kmock-jvm:${Version.antibytes.kmock}"
            }

            val js = JsTest

            object JsTest {
                const val annotations = "tech.antibytes.test-utils-kmp:test-utils-annotations-js:${Version.antibytes.test}"
                const val core = "tech.antibytes.test-utils-kmp:test-utils-js:${Version.antibytes.test}"
                const val fixture = "tech.antibytes.kfixture:core-js:${Version.antibytes.kfixture}"
                const val coroutine = "tech.antibytes.test-utils-kmp:test-utils-coroutine-js:${Version.antibytes.test}"
                const val kmock = "tech.antibytes.kmock:kmock-js:${Version.antibytes.kmock}"
            }

            val android = AndroidTest

            object AndroidTest {
                const val annotations = "tech.antibytes.test-utils-kmp:test-utils-annotations-android:${Version.antibytes.test}"
                const val core = "tech.antibytes.test-utils-kmp:test-utils-android:${Version.antibytes.test}"
                const val fixture = "tech.antibytes.kfixture:core-android:${Version.antibytes.kfixture}"
                const val coroutine = "tech.antibytes.test-utils-kmp:test-utils-coroutine-android:${Version.antibytes.test}"
                const val kmock = "tech.antibytes.kmock:kmock-android:${Version.antibytes.kmock}"
            }
        }
    }
}
