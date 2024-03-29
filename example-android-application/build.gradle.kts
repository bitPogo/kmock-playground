/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.dependency.Dependency
import tech.antibytes.kmock.example.dependency.Dependency as LocalDependency
import tech.antibytes.gradle.dependency.Version

plugins {
    id("org.jetbrains.kotlin.android")

    // Android
    id("com.android.application")

    // Contains the major part of the Android Setup
    id("tech.antibytes.gradle.configuration")

    id("tech.antibytes.kmock.kmock-gradle")
}

kmock {
    rootPackage = "tech.antibytes.kmock.example"
}

android {
    defaultConfig {
        applicationId = "tech.antibytes.kmock.example.app"
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Version.android.compose.compiler
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        debug {
            isMinifyEnabled = false
            matchingFallbacks.add("release")
        }
    }
}

dependencies {
    implementation(Dependency.multiplatform.kotlin.jdk8)

    implementation(Dependency.android.ktx.core)
    implementation(Dependency.android.ktx.viewmodel)
    implementation(Dependency.android.ktx.viewmodelCoroutine)
    implementation(Dependency.android.appCompact.core)

    implementation(Dependency.android.compose.ui)
    implementation(Dependency.android.compose.material)
    implementation(Dependency.android.compose.viewmodel)
    implementation(Dependency.android.compose.foundation)

    testImplementation(LocalDependency.antibytes.test.android.core)
    testImplementation(LocalDependency.antibytes.test.android.coroutine)
    testImplementation(LocalDependency.antibytes.test.android.fixture)
    testImplementation(LocalDependency.antibytes.test.android.kmock)
    testImplementation(Dependency.android.test.junit)
    testImplementation(Dependency.android.test.junit4)

    // Debug
    debugImplementation(Dependency.android.compose.uiTooling)
    debugImplementation(Dependency.android.compose.uiManifest)

    androidTestImplementation(Dependency.android.test.junit)
    androidTestImplementation(Dependency.android.test.junit4)
    androidTestImplementation(Dependency.android.test.composeJunit4)
    androidTestImplementation(Dependency.android.test.espressoCore)
    androidTestImplementation(Dependency.android.test.uiAutomator)

    androidTestImplementation(LocalDependency.antibytes.test.android.core)
    androidTestImplementation(LocalDependency.antibytes.test.android.fixture)
    androidTestImplementation(LocalDependency.antibytes.test.android.kmock)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
