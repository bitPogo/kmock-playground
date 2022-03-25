/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.dependency.Version
import tech.antibytes.kmock.example.dependency.addCustomRepositories

plugins {
    id("tech.antibytes.kmock.example.dependency")

    id("tech.antibytes.gradle.dependency")

    id("tech.antibytes.kmock.example.script.quality-spotless")

    id("org.owasp.dependencycheck")
}

allprojects {
    repositories {
        addCustomRepositories()
        repositories {
            maven {
                url = uri("https://maven.pkg.github.com/bitPogo/kmock")
                credentials {
                    username = project.findProperty("gpr.user") as String? ?: System.getenv("PACKAGE_REGISTRY_UPLOAD_USERNAME").toString()
                    password = project.findProperty("gpr.key") as String? ?: System.getenv("PACKAGE_REGISTRY_DOWNLOAD_TOKEN").toString()
                }
            }
        }

        mavenCentral()
        google()
        jcenter() // nodejs
    }

    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlin" && requested.name == "kotlin-stdlib-jdk8" && requested.version == "1.6.0") {
                useVersion(Version.kotlin.stdlib)
                because("Avoid resolution conflicts")
            }

            if (requested.group == "org.jetbrains.kotlin" && requested.name == "kotlin-stdlib-jdk8" && requested.version == "1.5.30") {
                useVersion(Version.kotlin.stdlib)
                because("Avoid resolution conflicts")
            }
        }
    }
}

tasks.named<Wrapper>("wrapper") {
    gradleVersion = "7.2"
    distributionType = Wrapper.DistributionType.ALL
}
