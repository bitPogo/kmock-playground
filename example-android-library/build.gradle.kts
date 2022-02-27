/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.dependency.Dependency
import tech.antibytes.kmock.example.dependency.Dependency as LocalDependency

plugins {
    id("org.jetbrains.kotlin.android")

    // Android
    id("com.android.library")

    id("tech.antibytes.gradle.configuration")

    id("tech.antibytes.kmock.kmock-gradle")
}

dependencies {
    implementation(Dependency.multiplatform.kotlin.jdk8)
    implementation(Dependency.multiplatform.coroutines.common)
    implementation(Dependency.multiplatform.stately.concurrency)

    testImplementation(LocalDependency.antibytes.test.android.core)
    testImplementation(LocalDependency.antibytes.test.android.coroutine)
    testImplementation(LocalDependency.antibytes.test.android.fixture)
    testImplementation(LocalDependency.antibytes.test.android.kmock)
    testImplementation(Dependency.android.test.junit)
    testImplementation(Dependency.android.test.junit4)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
