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
            const val util = "tech.antibytes.gradle-plugins:antibytes-gradle-utils:${Version.gradle.antibytes}"
            val kmock = "tech.antibytes.kmock-gradle:kmock:${Version.antibytes.kmock}"
        }

        val test = Test

        object Test {
            val kmockProcessor = "tech.antibytes.kmock:kmock-processor:${Version.antibytes.kmock}"
            val kmp = KmpTest

            object KmpTest {
                val annotations = "tech.antibytes.test-utils-kmp:test-utils-annotations:${Version.antibytes.test}"
                val core = "tech.antibytes.test-utils-kmp:test-utils:${Version.antibytes.test}"
                val fixture = "tech.antibytes.test-utils-kmp:test-utils-fixture:${Version.antibytes.test}"
                val coroutine = "tech.antibytes.test-utils-kmp:test-utils-coroutine:${Version.antibytes.test}"
                val ktor = "tech.antibytes.test-utils-kmp:test-utils-ktor:${Version.antibytes.test}"
                val gradle = "tech.antibytes.gradle-plugins:antibytes-gradle-test-utils:${Version.gradle.antibytes}"
                val kmock = "tech.antibytes.kmock:kmock:${Version.antibytes.kmock}"
            }

            val jvm = JvmTest

            object JvmTest {
                val annotations = "tech.antibytes.test-utils-kmp:test-utils-annotations-jvm:${Version.antibytes.test}"
                val core = "tech.antibytes.test-utils-kmp:test-utils-jvm:${Version.antibytes.test}"
                val fixture = "tech.antibytes.test-utils-kmp:test-utils-fixture-jvm:${Version.antibytes.test}"
                val coroutine = "tech.antibytes.test-utils-kmp:test-utils-coroutine-jvm:${Version.antibytes.test}"
                val ktor = "tech.antibytes.test-utils-kmp:test-utils-ktor-jvm:${Version.antibytes.test}"
                val gradle = "tech.antibytes.gradle-plugins:antibytes-gradle-test-utils-jvm:${Version.gradle.antibytes}"
                val kmock = "tech.antibytes.kmock:kmock-jvm:${Version.antibytes.kmock}"
            }

            val js = JsTest

            object JsTest {
                val annotations = "tech.antibytes.test-utils-kmp:test-utils-annotations-js:${Version.antibytes.test}"
                val core = "tech.antibytes.test-utils-kmp:test-utils-js:${Version.antibytes.test}"
                val fixture = "tech.antibytes.test-utils-kmp:test-utils-fixture-js:${Version.antibytes.test}"
                val coroutine = "tech.antibytes.test-utils-kmp:test-utils-coroutine-js:${Version.antibytes.test}"
                val ktor = "tech.antibytes.test-utils-kmp:test-utils-ktor-js:${Version.antibytes.test}"
                val gradle = "tech.antibytes.gradle-plugins:antibytes-gradle-test-utils-js:${Version.gradle.antibytes}"
                val kmock = "tech.antibytes.kmock:kmock-js:${Version.antibytes.kmock}"
            }

            val android = AndroidTest

            object AndroidTest {
                val annotations = "tech.antibytes.test-utils-kmp:test-utils-annotations-android:${Version.antibytes.test}"
                val core = "tech.antibytes.test-utils-kmp:test-utils-android:${Version.antibytes.test}"
                val fixture = "tech.antibytes.test-utils-kmp:test-utils-fixture-android:${Version.antibytes.test}"
                val coroutine = "tech.antibytes.test-utils-kmp:test-utils-coroutine-android:${Version.antibytes.test}"
                val ktor = "tech.antibytes.test-utils-kmp:test-utils-ktor-android:${Version.antibytes.test}"
                val gradle = "tech.antibytes.gradle-plugins:antibytes-gradle-test-utils-android:${Version.gradle.antibytes}"
                val kmock = "tech.antibytes.kmock:kmock-android:${Version.antibytes.kmock}"
            }
        }
    }
}
