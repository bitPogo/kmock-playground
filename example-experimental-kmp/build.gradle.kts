/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.dependency.Dependency
import tech.antibytes.kmock.example.dependency.Dependency as LocalDependency
import tech.antibytes.gradle.kmock.KMockExtension

plugins {
    id("org.jetbrains.kotlin.multiplatform")

    // Android
    id("com.android.library")

    id("tech.antibytes.gradle.configuration")

    id("tech.antibytes.kmock.kmock-gradle") apply false
}

kotlin {
    android()

    js(IR) {
        nodejs()
        browser()
    }

    jvm()

    ios()

    linuxX64()

    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.RequiresOptIn")
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(Dependency.multiplatform.kotlin.common)
                implementation(Dependency.multiplatform.coroutines.common)
                implementation(Dependency.multiplatform.stately.isolate)
                implementation(Dependency.multiplatform.stately.concurrency)

                implementation(LocalDependency.antibytes.test.kmp.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(Dependency.multiplatform.test.common)
                implementation(Dependency.multiplatform.test.annotations)

                implementation(LocalDependency.antibytes.test.kmp.annotations)
                implementation(LocalDependency.antibytes.test.kmp.coroutine)
                implementation(LocalDependency.antibytes.test.kmp.fixture)
                implementation(LocalDependency.antibytes.test.kmp.kmock)
            }
        }

        val concurrentMain by creating {
            dependsOn(commonMain)
        }

        val concurrentTest by creating {
            dependsOn(commonTest)
        }

        val androidMain by getting {
            dependsOn(concurrentMain)

            dependencies {
                implementation(Dependency.multiplatform.kotlin.android)
            }
        }
        val androidAndroidTestRelease by getting
        val androidTestFixtures by getting
        val androidTestFixturesDebug by getting
        val androidTestFixturesRelease by getting
        val androidTest by getting {
            dependsOn(androidAndroidTestRelease)
            dependsOn(androidTestFixtures)
            dependsOn(androidTestFixturesDebug)
            dependsOn(androidTestFixturesRelease)
            dependsOn(concurrentTest)

            dependencies {
                implementation(Dependency.multiplatform.test.jvm)
                implementation(Dependency.multiplatform.test.junit)
                implementation(Dependency.android.test.robolectric)
            }
        }

        val jsMain by getting {
            dependsOn(commonMain)

            dependencies {
                implementation(Dependency.multiplatform.kotlin.js)
                implementation(Dependency.js.nodejs)
            }
        }
        val jsTest by getting {
            dependsOn(commonTest)

            dependencies {
                implementation(Dependency.multiplatform.test.js)
            }
        }

        val jvmMain by getting {
            dependsOn(concurrentMain)

            dependencies {
                implementation(Dependency.multiplatform.kotlin.jdk8)
            }
        }
        val jvmTest by getting {
            dependsOn(concurrentTest)

            dependencies {
                implementation(Dependency.multiplatform.test.jvm)
                implementation(Dependency.multiplatform.test.junit)
            }
        }

        val nativeMain by creating {
            dependsOn(concurrentMain)
        }
        val nativeTest by creating {
            dependsOn(concurrentTest)
        }

        val darwinMain by creating {
            dependsOn(nativeMain)
        }
        val darwinTest by creating {
            dependsOn(nativeTest)
        }

        val otherMain by creating {
            dependsOn(nativeMain)
        }
        val otherTest by creating {
            dependsOn(nativeTest)
        }

        val linuxX64Main by getting {
            dependsOn(otherMain)
        }
        val linuxX64Test by getting {
            dependsOn(otherTest)
        }

        val iosMain by getting {
            dependsOn(darwinMain)
        }
        val iosTest by getting {
            dependsOn(darwinTest)
        }

        val iosX64Test by getting {
            dependsOn(iosTest)
        }
    }
}

plugins.apply("tech.antibytes.kmock.kmock-gradle")

project.extensions.configure<KMockExtension>("kmock") {
    rootPackage = "tech.antibytes.kmock.example"
    spyOn = setOf(
        "tech.antibytes.kmock.example.contract.ExampleContract.SampleDomainObject"
    )
    aliasNameMapping = mapOf(
        "tech.antibytes.kmock.example.contract.ConcurrentCollisionContract.ConcurrentThing" to "Alias",
        "tech.antibytes.kmock.example.contract.ConcurrentCollisionContract.SomethingGenericConcurrent" to "AliasGeneric"
    )
    allowExperimentalProxyAccess = true
}
