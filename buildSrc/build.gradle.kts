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
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/bitPogo/kmock")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("PACKAGE_REGISTRY_USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("PACKAGE_REGISTRY_DOWNLOAD_TOKEN")
            }
        }
    }
}

dependencies {
    implementation(Dependency.gradle.dependency)
    implementation(Dependency.gradle.spotless)
    implementation(Dependency.gradle.projectConfig)
    implementation(Dependency.gradle.kmock)
}
