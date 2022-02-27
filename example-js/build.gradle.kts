/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.dependency.Dependency
import tech.antibytes.kmock.example.dependency.Dependency as LocalDependency
import tech.antibytes.gradle.kmock.KMockExtension

plugins {
    id("org.jetbrains.kotlin.js")

    id("tech.antibytes.kmock.kmock-gradle") apply false
}

kotlin {
    js(IR) {
        nodejs()
        browser()
    }
}

plugins.apply("tech.antibytes.kmock.kmock-gradle")

project.extensions.configure(KMockExtension::class.java) {
    rootPackage = "tech.antibytes.kmock.example"
}

dependencies {
    implementation(Dependency.multiplatform.kotlin.js)
    implementation(Dependency.multiplatform.coroutines.common)
    implementation(Dependency.multiplatform.stately.concurrency)
    implementation(Dependency.js.nodejs)

    testImplementation(LocalDependency.antibytes.test.js.core)
    testImplementation(LocalDependency.antibytes.test.js.coroutine)
    testImplementation(LocalDependency.antibytes.test.js.fixture)
    testImplementation(LocalDependency.antibytes.test.js.kmock)
    testImplementation(Dependency.js.test.js)
}
