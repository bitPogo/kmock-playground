/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.kmock.example.dependency.Dependency
import tech.antibytes.kmock.example.dependency.addCustomRepositories

plugins {
    `kotlin-dsl`

    id("tech.antibytes.kmock.example.dependency")
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
    addCustomRepositories()
}

dependencies {
    implementation(Dependency.gradle.dependency)
    implementation(Dependency.gradle.coverage)
    implementation(Dependency.gradle.spotless)
    implementation(Dependency.gradle.projectConfig)
    implementation(Dependency.gradle.kmock)
}
