/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

includeBuild("gradlePlugin/kmock-playground-dependency")

plugins {
    id("com.gradle.enterprise") version("3.11.1")
}

include(
    ":example-android-library",
    ":example-android-application",
    ":example-js",
    ":example-jvm",
    ":example-kmp",
    ":example-kmp-relaxed",
    ":example-experimental-kmp"
)

buildCache {
    local {
        isEnabled = false
        directory = File(rootDir, "build-cache")
        removeUnusedEntriesAfterDays = 30
    }
}

rootProject.name = "kmock-playground"
