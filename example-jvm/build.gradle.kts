/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.dependency.Dependency
import tech.antibytes.kmock.example.dependency.Dependency as LocalDependency

plugins {
    id("org.jetbrains.kotlin.jvm")

    id("tech.antibytes.kmock.kmock-gradle")
}

dependencies {
    implementation(Dependency.multiplatform.kotlin.jdk8)
    implementation(Dependency.multiplatform.coroutines.common)
    implementation(Dependency.multiplatform.stately.concurrency)

    testImplementation(LocalDependency.antibytes.test.jvm.core)
    testImplementation(LocalDependency.antibytes.test.jvm.coroutine)
    testImplementation(LocalDependency.antibytes.test.jvm.fixture)
    testImplementation(LocalDependency.antibytes.test.jvm.kmock)
    testImplementation(Dependency.jvm.test.kotlin)
    testImplementation(platform(Dependency.jvm.test.junit))
    testImplementation(Dependency.jvm.test.jupiter)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.test {
    useJUnitPlatform()
}
